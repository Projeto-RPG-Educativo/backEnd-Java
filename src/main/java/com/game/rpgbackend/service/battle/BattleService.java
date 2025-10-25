package com.game.rpgbackend.service.battle;

import com.game.rpgbackend.config.GameConfig;
import com.game.rpgbackend.domain.Character;
import com.game.rpgbackend.domain.Monster;
import com.game.rpgbackend.domain.PlayerStats;
import com.game.rpgbackend.domain.Question;
import com.game.rpgbackend.dto.response.BattleStateResponse;
import com.game.rpgbackend.exception.BadRequestException;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.CharacterRepository;
import com.game.rpgbackend.repository.MonsterRepository;
import com.game.rpgbackend.repository.PlayerStatsRepository;
import com.game.rpgbackend.repository.QuestionRepository;
import com.game.rpgbackend.service.character.CharacterService;
import com.game.rpgbackend.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço principal de batalha que orquestra todo o sistema de combate.
 */
@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleStateService battleStateService;
    private final CombatService combatService;
    private final QuestionService questionService;
    private final CharacterService characterService;
    private final CharacterRepository characterRepository;
    private final MonsterRepository monsterRepository;
    private final QuestionRepository questionRepository;
    private final GameConfig gameConfig;
    private final PlayerStatsRepository playerStatsRepository;


    /**
     * Orquestra a ação de ATAQUE.
     */
    @Transactional
    public BattleStateResponse attack(Integer userId) {
        BattleStateResponse battle = battleStateService.getActiveBattle(userId);
        if (battle == null) {
            throw new NotFoundException("Nenhuma batalha ativa encontrada.");
        }

        // 1. Verificar energia
        if (battle.getCharacter().getEnergy() < gameConfig.getCosts().getAttack()) {
            throw new BadRequestException("Energia insuficiente para atacar!");
        }
        battle.getCharacter().setEnergy(battle.getCharacter().getEnergy() - gameConfig.getCosts().getAttack());

        // 2. Delega o cálculo para o combatService
        CombatService.AttackResult result = combatService.performAttack(battle.getCharacter());
        battle.getMonster().setHp(battle.getMonster().getHp() - result.getDamageDealt());

        // 3. Persiste a nova energia no banco de dados
        characterRepository.findById(battle.getCharacter().getId()).ifPresent(character -> {
            character.setEnergy(battle.getCharacter().getEnergy());
            characterRepository.save(character);
        });

        // 4. Atualiza o estado da batalha na memória
        battleStateService.setActiveBattle(userId, battle);

        // 5. Retorna o estado atualizado com a mensagem do turno
        battle.setTurnResult(result.getTurnResult());
        return battle;
    }

    /**
     * Orquestra a ação de DEFESA.
     */
    @Transactional
    public BattleStateResponse defend(Integer userId) {
        BattleStateResponse battle = battleStateService.getActiveBattle(userId);
        if (battle == null) {
            throw new NotFoundException("Nenhuma batalha ativa encontrada.");
        }

        // 1. Verificar energia
        if (battle.getCharacter().getEnergy() < gameConfig.getCosts().getDefend()) {
            throw new BadRequestException("Energia insuficiente para defender!");
        }
        battle.getCharacter().setEnergy(battle.getCharacter().getEnergy() - gameConfig.getCosts().getDefend());

        // 2. Delega a lógica para o combatService
        CombatService.DefenseResult result = combatService.performDefense(battle.getCharacter());
        battle.setCharacter(result.getUpdatedCharacter());

        // 3. Persiste a nova energia no banco de dados
        characterRepository.findById(battle.getCharacter().getId()).ifPresent(character -> {
            character.setEnergy(battle.getCharacter().getEnergy());
            characterRepository.save(character);
        });

        // 4. Atualiza o estado da batalha na memória
        battleStateService.setActiveBattle(userId, battle);

        // 5. Retorna o estado atualizado
        battle.setTurnResult(result.getTurnResult());
        return battle;
    }

    /**
     * Orquestra a ação de USAR HABILIDADE.
     */
    @Transactional
    public BattleStateResponse useSkill(Integer userId) {
        BattleStateResponse battle = battleStateService.getActiveBattle(userId);
        if (battle == null) {
            throw new NotFoundException("Nenhuma batalha ativa encontrada.");
        }

        if (battle.getCharacter().getEnergy() < gameConfig.getCosts().getAbility()) {
            throw new BadRequestException("Energia insuficiente para usar a habilidade!");
        }
        battle.getCharacter().setEnergy(battle.getCharacter().getEnergy() - gameConfig.getCosts().getAbility());

        CombatService.SkillResult result = combatService.performSkill(battle.getCharacter());
        battle.setCharacter(result.getUpdatedCharacter());
        String turnResult = result.getTurnResult();

        // Orquestração dos Efeitos Especiais
        if (result.getEffect() != null) {
            switch (result.getEffect().getType()) {
                case "REMOVE_WRONG_ANSWER": // Efeito do Mago
                    Question question = questionRepository.findById(battle.getCurrentQuestion().getId())
                        .orElse(null);
                    if (question != null) {
                        String correctAnswer = question.getCorrectAnswer();
                        List<String> opcoes = new ArrayList<>(battle.getCurrentQuestion().getOpcoes());

                        // Remove primeira opção incorreta
                        opcoes.removeIf(opt -> !opt.equalsIgnoreCase(correctAnswer));
                        if (opcoes.size() < battle.getCurrentQuestion().getOpcoes().size()) {
                            battle.getCurrentQuestion().setOpcoes(opcoes);
                            turnResult += " Uma opção incorreta foi eliminada!";
                        }
                    }
                    break;

                case "PROVIDE_HINT": // Efeito do Ladino
                    Question hintQuestion = questionRepository.findById(battle.getCurrentQuestion().getId())
                        .orElse(null);
                    if (hintQuestion != null) {
                        String hint = String.format(" Dica: A resposta começa com a letra '%s'.",
                            hintQuestion.getCorrectAnswer().charAt(0));
                        turnResult += hint;
                    }
                    break;

                case "BARD_CHALLENGE": // Efeito do Bardo
                    long questionCount = questionRepository.count();
                    int skip = (int) (Math.random() * questionCount);
                    Question challengeQuestion = questionRepository.findAll().stream()
                        .skip(skip)
                        .findFirst()
                        .orElse(null);

                    if (challengeQuestion != null) {
                        BattleStateResponse.QuestionInfo questionInfo = new BattleStateResponse.QuestionInfo();
                        questionInfo.setId(challengeQuestion.getId());
                        questionInfo.setTexto(challengeQuestion.getQuestionText());
                        List<String> options = List.of(
                            challengeQuestion.getOptionA(),
                            challengeQuestion.getOptionB(),
                            challengeQuestion.getOptionC()
                        );
                        questionInfo.setOpcoes(options);
                        battle.setCurrentQuestion(questionInfo);
                        battle.setBardChallengeActive(true);
                    }
                    break;
            }
        }

        // Persiste as mudanças
        characterRepository.findById(battle.getCharacter().getId()).ifPresent(character -> {
            character.setHp(battle.getCharacter().getHp());
            character.setEnergy(battle.getCharacter().getEnergy());
            characterRepository.save(character);
        });

        battleStateService.setActiveBattle(userId, battle);
        battle.setTurnResult(turnResult);
        return battle;
    }

    /**
     * Inicia uma nova batalha.
     */
    @Transactional
    public BattleStateResponse startBattle(Integer userId, Integer monsterId, String difficulty) {
        // 1. Busca dados do banco
        Character character = characterRepository.findByUserIdOrderByIdDesc(userId)
            .stream()
            .findFirst()
            .orElseThrow(() -> new BadRequestException("Personagem não encontrado"));

        Monster monster = monsterRepository.findById(monsterId)
            .orElseThrow(() -> new NotFoundException("Monstro não encontrado"));

        Integer playerLevel = character.getUser().getStats() != null
            ? character.getUser().getStats().getLevel()
            : 1;

        Question firstQuestion = questionService.getRandomQuestion(difficulty, playerLevel, null);

        if (character.getGameClass() == null) {
            throw new BadRequestException("Dados insuficientes para iniciar a batalha.");
        }

        // 2. Monta o estado inicial da batalha
        BattleStateResponse battleState = new BattleStateResponse();
        battleState.setDifficulty(difficulty);
        battleState.setBattleId(System.currentTimeMillis());

        BattleStateResponse.CharacterBattleInfo charInfo = new BattleStateResponse.CharacterBattleInfo();
        charInfo.setId(character.getId());
        charInfo.setHp(character.getHp());
        charInfo.setEnergy(character.getEnergy());
        charInfo.setClassName(character.getGameClass().getName());
        charInfo.setStrength(character.getGameClass().getStrength());
        charInfo.setIntelligence(character.getGameClass().getIntelligence());
        charInfo.setLevel(playerLevel);
        charInfo.setXp(character.getXp());
        battleState.setCharacter(charInfo);

        BattleStateResponse.MonsterBattleInfo monsterInfo = new BattleStateResponse.MonsterBattleInfo();
        monsterInfo.setId(monster.getId());
        monsterInfo.setHp(monster.getHp());
        monsterInfo.setDano(monster.getMonsterDamage());
        monsterInfo.setNome(monster.getMonsterName());
        battleState.setMonster(monsterInfo);

        BattleStateResponse.QuestionInfo questionInfo = new BattleStateResponse.QuestionInfo();
        questionInfo.setId(firstQuestion.getId());
        questionInfo.setTexto(firstQuestion.getQuestionText());
        questionInfo.setNivelMinimo(firstQuestion.getMinLevel());
        questionInfo.setDifficulty(firstQuestion.getDifficulty());
        List<String> options = List.of(
            firstQuestion.getOptionA(),
            firstQuestion.getOptionB(),
            firstQuestion.getOptionC()
        );
        questionInfo.setOpcoes(options);
        battleState.setCurrentQuestion(questionInfo);

        battleState.setIsFinished(false);

        // 3. Salva o estado
        battleStateService.setActiveBattle(userId, battleState);

        return battleState;
    }

    /**
     * Processa a resposta do jogador.
     */
    @Transactional
    public BattleStateResponse processAnswer(Integer userId, Long battleId, Integer questionId, String answer) {
        // 1. Obtém o estado atual
        BattleStateResponse battle = battleStateService.getActiveBattle(userId);
        if (battle == null || !battle.getBattleId().equals(battleId) || battle.getIsFinished()) {
            throw new BadRequestException("Batalha inválida ou já finalizada.");
        }

        // 2. Busca a resposta correta
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new NotFoundException("Pergunta não encontrada."));

        boolean isCorrect = answer.trim().equalsIgnoreCase(question.getCorrectAnswer().trim());

        // 2.1. Busca as estatísticas do jogador
        Character character = characterRepository.findById(battle.getCharacter().getId())
            .orElseThrow(() -> new NotFoundException("Personagem não encontrado"));

        PlayerStats stats = playerStatsRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Estatísticas não encontradas"));

        // LÓGICA DO DESAFIO DO BARDO
        if (Boolean.TRUE.equals(battle.getBardChallengeActive())) {
            if (isCorrect) {
                int xpReward = gameConfig.getBattle().getXpWinReward() * 2;

                // Atualiza estatísticas do Bardo (vitória + questão correta)
                stats.setQuestionsRight(stats.getQuestionsRight() + 1);
                stats.setBattlesWon(stats.getBattlesWon() + 1);
                stats.setTotalXpEarned(stats.getTotalXpEarned() + xpReward);
                playerStatsRepository.save(stats);

                characterRepository.findById(battle.getCharacter().getId()).ifPresent(ch -> {
                    ch.setXp(ch.getXp() + xpReward);
                    characterRepository.save(ch);
                });
                battleStateService.removeActiveBattle(userId);
                battle.setIsFinished(true);
                battle.setTurnResult("Incrível! Sua Lábia funcionou e você encerrou o combate com maestria, ganhando o dobro de XP!");
                return battle;
            } else {
                // Atualiza estatísticas do Bardo (derrota + questão errada)
                stats.setQuestionsWrong(stats.getQuestionsWrong() + 1);
                stats.setBattlesLost(stats.getBattlesLost() + 1);
                playerStatsRepository.save(stats);

                battle.getCharacter().setHp(battle.getCharacter().getHp() - 30);
                battleStateService.removeActiveBattle(userId);
                battle.setIsFinished(true);
                battle.setTurnResult("Sua Lábia falhou! Você irritou o monstro e foi derrotado.");
                return battle;
            }
        }

        // 2.2. Atualiza estatísticas da questão
        if (isCorrect) {
            stats.setQuestionsRight(stats.getQuestionsRight() + 1);
        } else {
            stats.setQuestionsWrong(stats.getQuestionsWrong() + 1);
        }
        playerStatsRepository.save(stats);

        // 3. Processa o turno
        CombatService.TurnResult turn = combatService.processAnswerTurn(battle, isCorrect);
        BattleStateResponse updatedBattle = turn.getUpdatedBattleState();
        String turnResult = turn.getTurnResult();

        // 4. Verifica se a batalha terminou
        if (updatedBattle.getMonster().getHp() <= 0) {
            updatedBattle.setIsFinished(true);
            turnResult += " Você venceu a batalha!";

            // Incrementar batalhas vencidas e XP total
            stats.setBattlesWon(stats.getBattlesWon() + 1);
            stats.setTotalXpEarned(stats.getTotalXpEarned() + gameConfig.getBattle().getXpWinReward());
            playerStatsRepository.save(stats);

            characterRepository.findById(updatedBattle.getCharacter().getId()).ifPresent(ch -> {
                ch.setXp(ch.getXp() + gameConfig.getBattle().getXpWinReward());
                characterRepository.save(ch);
            });

            CharacterService.LevelUpResult levelUpResult = characterService.checkForLevelUp(
                updatedBattle.getCharacter().getId()
            );
            turnResult += " " + levelUpResult.getMessage();

            battleStateService.removeActiveBattle(userId);
        } else if (updatedBattle.getCharacter().getHp() <= 0) {
            updatedBattle.setIsFinished(true);
            turnResult += " Você foi derrotado.";

            // Incrementar batalhas perdidas
            stats.setBattlesLost(stats.getBattlesLost() + 1);
            playerStatsRepository.save(stats);

            battleStateService.removeActiveBattle(userId);
        }

        // 5. Se continuar, busca nova pergunta
        if (!updatedBattle.getIsFinished()) {
            Integer playerLevel = character.getUser().getStats() != null
                ? character.getUser().getStats().getLevel()
                : 1;

            Question nextQuestion = questionService.getRandomQuestion(
                updatedBattle.getDifficulty(),
                playerLevel,
                null
            );

            if (nextQuestion == null) {
                throw new BadRequestException("Não foi possível carregar a próxima pergunta.");
            }

            BattleStateResponse.QuestionInfo questionInfo = new BattleStateResponse.QuestionInfo();
            questionInfo.setId(nextQuestion.getId());
            questionInfo.setTexto(nextQuestion.getQuestionText());
            questionInfo.setNivelMinimo(nextQuestion.getMinLevel());
            questionInfo.setDifficulty(nextQuestion.getDifficulty());
            List<String> options = List.of(
                nextQuestion.getOptionA(),
                nextQuestion.getOptionB(),
                nextQuestion.getOptionC()
            );
            questionInfo.setOpcoes(options);
            updatedBattle.setCurrentQuestion(questionInfo);

            battleStateService.setActiveBattle(userId, updatedBattle);
        }

        updatedBattle.setTurnResult(turnResult);
        return updatedBattle;
    }

    /**
     * Retorna a batalha ativa de um usuário.
     */
    public BattleStateResponse getActiveBattle(Integer userId) {
        return battleStateService.getActiveBattle(userId);
    }
}

