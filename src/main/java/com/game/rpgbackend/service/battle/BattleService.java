package com.game.rpgbackend.service.battle;

import com.game.rpgbackend.config.GameConfig;
import com.game.rpgbackend.domain.Character;
import com.game.rpgbackend.domain.Monster;
import com.game.rpgbackend.domain.PlayerStats;
import com.game.rpgbackend.domain.Question;
import com.game.rpgbackend.dto.response.battle.BattleStateResponse;
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
 * <p>
 * Responsável por gerenciar o fluxo completo de uma batalha, incluindo:
 * - Início de novas batalhas
 * - Processamento de ações de combate (ataque, defesa, habilidades)
 * - Sistema de turnos (jogador e monstro)
 * - Recuperação de energia através de questões
 * - Finalização de batalhas (vitória/derrota)
 * - Salvamento de resultados e atualização de estatísticas
 * </p>
 * <p>
 * O sistema de batalha funciona com turnos alternados onde o jogador
 * executa uma ação e o monstro responde. Questões educacionais podem
 * ser respondidas entre turnos para recuperar energia.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
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
    private final com.game.rpgbackend.service.hub.QuestService questService;


    /**
     * Executa a ação de ataque do personagem contra o monstro.
     * <p>
     * Consome energia do personagem e prepara o dano a ser causado.
     * O dano só é efetivamente aplicado após o turno do monstro.
     * Verifica se é o turno do jogador e se ele possui energia suficiente.
     * </p>
     *
     * @param userId identificador do usuário realizando a ação
     * @return estado atualizado da batalha com ação de ataque pendente
     * @throws NotFoundException se não houver batalha ativa
     * @throws BadRequestException se o jogador estiver atordoado, sem energia ou não for seu turno
     */
    @Transactional
    public BattleStateResponse attack(Integer userId) {
        BattleStateResponse battle = battleStateService.getActiveBattle(userId);
        if (battle == null) {
            throw new NotFoundException("Nenhuma batalha ativa encontrada.");
        }

        // Verifica se o jogador está atordoado
        if (combatService.hasCharacterEffect(battle, "STUN")) {
            throw new BadRequestException("Você está atordoado e não pode agir neste turno!");
        }

        // 0. Verificar se é o turno do jogador
        if (!Boolean.TRUE.equals(battle.getIsPlayerTurn())) {
            throw new BadRequestException("Não é o seu turno! Aguarde o turno do monstro.");
        }

        // 1. Verificar energia
        if (battle.getCharacter().getEnergy() < gameConfig.getCosts().getAttack()) {
            throw new BadRequestException("Energia insuficiente para atacar!");
        }
        battle.getCharacter().setEnergy(battle.getCharacter().getEnergy() - gameConfig.getCosts().getAttack());

        // 2. Delega o cálculo para o combatService (mas NÃO aplica o dano ainda)
        CombatService.AttackResult result = combatService.performAttack(battle.getCharacter());

        // Armazena o dano como pendente (será aplicado após a ação do monstro)
        battle.setPendingDamageToMonster(result.getDamageDealt());
        // NÃO mostra o dano ainda, pois não foi aplicado
        battle.setCharacterDamageDealt(0);
        battle.setMonsterDamageDealt(0);
        battle.setMonsterAction(null);

        String turnResult = result.getTurnResult();

        // 3. Batalha continua - marca que está aguardando turno do monstro e consome o turno do jogador
        battle.setWaitingForMonsterTurn(true);
        battle.setIsPlayerTurn(false); // Turno consumido

        // Persiste a nova energia no banco de dados
        characterRepository.findById(battle.getCharacter().getId()).ifPresent(character -> {
            character.setEnergy(battle.getCharacter().getEnergy());
            characterRepository.save(character);
        });

        // Atualiza o estado da batalha na memória
        battleStateService.setActiveBattle(userId, battle);

        // 4. Retorna o estado atualizado com a mensagem do turno
        battle.setTurnResult(turnResult);
        return battle;
    }

    /**
     * Executa a ação de defesa do personagem.
     * <p>
     * Consome energia e coloca o personagem em postura defensiva.
     * Reduz significativamente o dano recebido no próximo ataque do monstro.
     * A defesa dura apenas o turno atual e é removida automaticamente
     * se o monstro não causar dano.
     * </p>
     *
     * @param userId identificador do usuário realizando a ação
     * @return estado atualizado da batalha em postura defensiva
     * @throws NotFoundException se não houver batalha ativa
     * @throws BadRequestException se o jogador estiver atordoado, sem energia ou não for seu turno
     */
    @Transactional
    public BattleStateResponse defend(Integer userId) {
        BattleStateResponse battle = battleStateService.getActiveBattle(userId);
        if (battle == null) {
            throw new NotFoundException("Nenhuma batalha ativa encontrada.");
        }

        // 0. Verificar se é o turno do jogador
        if (!Boolean.TRUE.equals(battle.getIsPlayerTurn())) {
            throw new BadRequestException("Não é o seu turno! Aguarde o turno do monstro.");
        }

        // Verifica se o jogador está atordoado
        if (combatService.hasCharacterEffect(battle, "STUN")) {
            throw new BadRequestException("Você está atordoado e não pode agir neste turno!");
        }

        // 1. Verificar energia
        if (battle.getCharacter().getEnergy() < gameConfig.getCosts().getDefend()) {
            throw new BadRequestException("Energia insuficiente para defender!");
        }
        battle.getCharacter().setEnergy(battle.getCharacter().getEnergy() - gameConfig.getCosts().getDefend());

        // 2. Delega a lógica para o combatService
        CombatService.DefenseResult result = combatService.performDefense(battle.getCharacter());
        battle.setCharacter(result.getUpdatedCharacter());
        // Limpa os danos (ainda não houve combate)
        battle.setCharacterDamageDealt(0);
        battle.setMonsterDamageDealt(0);
        battle.setMonsterAction(null);
        battle.setPendingDamageToMonster(0);

        String turnResult = result.getTurnResult();

        // 3. Marca que está aguardando turno do monstro e consome o turno do jogador
        battle.setWaitingForMonsterTurn(true);
        battle.setIsPlayerTurn(false); // Turno consumido

        // 4. Persiste a nova energia no banco de dados
        characterRepository.findById(battle.getCharacter().getId()).ifPresent(character -> {
            character.setEnergy(battle.getCharacter().getEnergy());
            characterRepository.save(character);
        });

        // 5. Atualiza o estado da batalha na memória
        battleStateService.setActiveBattle(userId, battle);

        // 6. Retorna o estado atualizado
        battle.setTurnResult(turnResult);
        return battle;
    }

    /**
     * Executa a habilidade especial da classe do personagem.
     * <p>
     * Cada classe possui uma habilidade única:
     * - Lutador: Investida (dano massivo ignorando defesa parcial)
     * - Mago: Bola de Fogo (dano mágico)
     * - Bardo: Canto Inspirador (aumenta dano de ataques)
     * - Ladino: Ataque Furtivo (dano crítico)
     * - Paladino: Cura Divina (restaura HP)
     * - Tank: Provocar (força monstro a atacar)
     * </p>
     * <p>
     * Habilidades podem ser bloqueadas por efeitos como "DISABLE_SKILL"
     * e consomem mais energia que ações básicas.
     * </p>
     *
     * @param userId identificador do usuário realizando a ação
     * @return estado atualizado da batalha após usar a habilidade
     * @throws NotFoundException se não houver batalha ativa
     * @throws BadRequestException se o jogador estiver atordoado, sem energia,
     *                            com habilidades bloqueadas ou não for seu turno
     */
    @Transactional
    public BattleStateResponse useSkill(Integer userId) {
        BattleStateResponse battle = battleStateService.getActiveBattle(userId);
        if (battle == null) {
            throw new NotFoundException("Nenhuma batalha ativa encontrada.");
        }

        // 0. Verificar se é o turno do jogador
        if (!Boolean.TRUE.equals(battle.getIsPlayerTurn())) {
            throw new BadRequestException("Não é o seu turno! Aguarde o turno do monstro.");
        }

        // Verifica se o jogador está atordoado
        if (combatService.hasCharacterEffect(battle, "STUN")) {
            throw new BadRequestException("Você está atordoado e não pode agir neste turno!");
        }

        // Verifica se as habilidades estão desabilitadas (Amnesia Blast do Malak)
        if (combatService.hasCharacterEffect(battle, "DISABLE_SKILL")) {
            throw new BadRequestException("Suas habilidades de classe estão bloqueadas! Você não pode usá-las neste turno.");
        }

        if (battle.getCharacter().getEnergy() < gameConfig.getCosts().getAbility()) {
            throw new BadRequestException("Energia insuficiente para usar a habilidade!");
        }
        battle.getCharacter().setEnergy(battle.getCharacter().getEnergy() - gameConfig.getCosts().getAbility());

        CombatService.SkillResult result = combatService.performSkill(battle.getCharacter());
        battle.setCharacter(result.getUpdatedCharacter());
        // Limpa os danos (ainda não houve combate)
        battle.setCharacterDamageDealt(0);
        battle.setMonsterDamageDealt(0);
        battle.setMonsterAction(null);

        String turnResult = result.getTurnResult();

        // Verifica se é a Investida do Lutador (dano pendente)
        if (battle.getCharacter().getEffects() != null &&
            battle.getCharacter().getEffects().containsKey("isChargeActive")) {

            Integer baseDamage = (Integer) battle.getCharacter().getEffects().get("chargeBaseDamage");
            // Define dano pendente como se fosse um ataque normal
            battle.setPendingDamageToMonster(baseDamage);
        } else {
            battle.setPendingDamageToMonster(0);
        }

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
                    // First, get the ID of the current question.
                    // Ensure battle.getCurrentQuestion() is not null and has an ID.
                    if (battle.getCurrentQuestion() != null && battle.getCurrentQuestion().getId() != null) {
                        Integer currentQuestionId = battle.getCurrentQuestion().getId();
                        try {
                            // Call QuestionService to get the real hint from the database
                            String hint = questionService.getHintForQuestion(currentQuestionId); // Assuming questionService is injected

                            if (hint != null && !hint.isEmpty()) {
                                // Add the fetched hint to the turn result
                                turnResult += " Dica: " + hint;
                            } else {
                                // Handle cases where the question exists but has no hint
                                turnResult += " Nenhuma dica disponível para esta pergunta.";
                                // Optional: Consider refunding the skill cost here if no hint was provided
                                // attacker.setEnergy(attacker.getEnergy() + skill.getCost());
                            }
                        } catch (NotFoundException e) {
                            // Handle case where the question ID is somehow invalid (shouldn't normally happen here)
                            turnResult += " Erro: Pergunta atual não encontrada para obter dica.";
                            // Optional: Refund skill cost
                            // attacker.setEnergy(attacker.getEnergy() + skill.getCost());
                        } catch (Exception e) {
                            // Catch any other unexpected errors during the hint fetching
                            turnResult += " Erro inesperado ao obter a dica.";
                            System.err.println("Erro em PROVIDE_HINT: " + e.getMessage());
                            // Optional: Refund skill cost
                            // attacker.setEnergy(attacker.getEnergy() + skill.getCost());
                        }
                    } else {
                        // Handle case where there's no current question in the battle state
                        turnResult += " Não há pergunta ativa para obter dica.";
                        // Optional: Refund skill cost
                        // attacker.setEnergy(attacker.getEnergy() + skill.getCost());
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

        // Marca que está aguardando turno do monstro (exceto para BARD_CHALLENGE que é especial)
        if (result.getEffect() != null && "BARD_CHALLENGE".equals(result.getEffect().getType())) {
            // Bardo: não executa turno do monstro, aguarda resposta do desafio (não consome turno)
            battle.setWaitingForMonsterTurn(false);
            battle.setIsPlayerTurn(true); // Mantém o turno do jogador
        } else {
            // Outras skills: aguarda chamada do endpoint de turno do monstro (consome turno)
            battle.setWaitingForMonsterTurn(true);
            battle.setIsPlayerTurn(false); // Turno consumido
        }

        // Verifica se a batalha terminou após a skill
        if (battle.getMonster().getHp() <= 0) {
            battle.setIsFinished(true);
            turnResult += " Você venceu a batalha!";

            // Busca as estatísticas do jogador
            PlayerStats stats = playerStatsRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Estatísticas não encontradas"));
            stats.setBattlesWon(stats.getBattlesWon() + 1);
            stats.setTotalXpEarned(stats.getTotalXpEarned() + gameConfig.getBattle().getXpWinReward());
            playerStatsRepository.save(stats);

            // Adiciona XP ao personagem
            characterRepository.findById(battle.getCharacter().getId()).ifPresent(ch -> {
                ch.setXp(ch.getXp() + gameConfig.getBattle().getXpWinReward());
                characterRepository.save(ch);
            });

            // Verifica level up
            CharacterService.LevelUpResult levelUpResult = characterService.checkForLevelUp(battle.getCharacter().getId());
            turnResult += " " + levelUpResult.getMessage();

            battleStateService.removeActiveBattle(userId);
        } else {
            // Batalha continua - persiste as mudanças
            characterRepository.findById(battle.getCharacter().getId()).ifPresent(character -> {
                character.setHp(battle.getCharacter().getHp());
                character.setEnergy(battle.getCharacter().getEnergy());
                characterRepository.save(character);
            });

            battleStateService.setActiveBattle(userId, battle);
        }

        battle.setTurnResult(turnResult);
        return battle;
    }

    /**
     * Inicia uma nova batalha.
     */
    /**
     * Inicia uma nova batalha entre um personagem e um monstro.
     * <p>
     * Cria o estado inicial da batalha com:
     * - HP do personagem restaurado ao máximo
     * - Energia do personagem restaurada ao máximo
     * - HP e estatísticas base do monstro
     * - Primeira questão educacional gerada
     * - Sistema de turnos inicializado (jogador começa)
     * </p>
     * <p>
     * A dificuldade influencia a complexidade das questões apresentadas.
     * </p>
     *
     * @param userId identificador do usuário iniciando a batalha
     * @param monsterId identificador do monstro a ser enfrentado
     * @param difficulty nível de dificuldade (easy, medium, hard)
     * @param characterId identificador do personagem que lutará
     * @return estado inicial da batalha pronto para começar
     * @throws BadRequestException se o personagem não pertencer ao usuário ou dados inválidos
     * @throws NotFoundException se o monstro ou personagem não for encontrado
     */
    @Transactional
    public BattleStateResponse startBattle(Integer userId, Integer monsterId, String difficulty, Integer characterId) {
        // 1. Busca dados do banco
        Character character = characterRepository.findById(characterId)
            .filter(c -> c.getUser().getId().equals(userId))
            .orElseThrow(() -> new BadRequestException("Personagem não encontrado ou não pertence ao usuário"));

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
        charInfo.setMaxHp(character.getHp());
        charInfo.setEnergy(character.getMaxEnergy());
        charInfo.setMaxEnergy(character.getMaxEnergy());
        charInfo.setClassName(character.getGameClass().getName());
        charInfo.setStrength(character.getGameClass().getStrength());
        charInfo.setIntelligence(character.getGameClass().getIntelligence());
        charInfo.setDefense(character.getGameClass().getDefense());
        charInfo.setLevel(playerLevel);
        charInfo.setXp(character.getXp());
        int maxXpForLevel = (int) (gameConfig.getLeveling().getBaseXp() * Math.pow(gameConfig.getLeveling().getXpMultiplier(), playerLevel));
        charInfo.setMaxXpForLevel(maxXpForLevel);
        battleState.setCharacter(charInfo);

        BattleStateResponse.MonsterBattleInfo monsterInfo = new BattleStateResponse.MonsterBattleInfo();
        monsterInfo.setId(monster.getId());
        monsterInfo.setHp(monster.getHp());
        monsterInfo.setMaxHp(monster.getHp());
        monsterInfo.setDano(monster.getMonsterDamage());
        monsterInfo.setDefense(monster.getDefense());
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

        // Busca quests ativas do personagem
        System.out.println("DEBUG BattleService: Buscando quests para character.getId() = " + character.getId());
        List<com.game.rpgbackend.dto.response.hub.QuestDto> activeQuests =
            questService.getActiveQuestsByCharacterId(character.getId());
        System.out.println("DEBUG BattleService: Quests retornadas: " + (activeQuests != null ? activeQuests.size() : "null"));
        battleState.setActiveQuests(activeQuests);

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

        // 1.1. Verifica se é o turno do jogador
        if (!Boolean.TRUE.equals(battle.getIsPlayerTurn())) {
            throw new BadRequestException("Não é o seu turno! Aguarde o turno do monstro.");
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

            // Atualiza progresso de quests de ANSWER_QUESTIONS e obtém lista atualizada
            try {
                List<com.game.rpgbackend.dto.response.hub.QuestDto> updatedQuests =
                    questService.updateQuestionProgressForAllActiveQuests(character.getId());
                battle.setActiveQuests(updatedQuests);
            } catch (Exception e) {
                System.err.println("Erro ao atualizar progresso de quest: " + e.getMessage());
            }
        } else {
            stats.setQuestionsWrong(stats.getQuestionsWrong() + 1);
        }
        playerStatsRepository.save(stats);

        // 3. Processa o turno
        CombatService.TurnResult turn = combatService.processAnswerTurn(battle, isCorrect);
        BattleStateResponse updatedBattle = turn.getUpdatedBattleState();
        String turnResult = turn.getTurnResult();
        updatedBattle.setCharacterDamageDealt(0);
        updatedBattle.setMonsterDamageDealt(0);
        updatedBattle.setMonsterAction(null);

        // Se errou a pergunta, o monstro faz seu turno e consome o turno do jogador
        if (!isCorrect) {
            CombatService.MonsterTurnResult monsterResult = combatService.performMonsterTurn(updatedBattle);
            turnResult += " " + monsterResult.getTurnResult();
            updatedBattle.setMonsterDamageDealt(monsterResult.getDamageDealt());
            updatedBattle.setMonsterAction(monsterResult.getAction());
            // Turno foi consumido (monstro atacou), mas devolve ao jogador
            updatedBattle.setIsPlayerTurn(true);
        } else {
            // Se acertou, recupera energia mas mantém o turno
            updatedBattle.setIsPlayerTurn(true);
        }

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

            // Atualiza progresso de quests WIN_BATTLES e DEFEAT_MONSTER
            try {
                // Primeiro atualiza quest de monstro específico
                questService.updateMonsterDefeatProgress(character.getId(), updatedBattle.getMonster().getId());
                // Depois atualiza quest de vitórias e obtém lista completa atualizada
                List<com.game.rpgbackend.dto.response.hub.QuestDto> updatedQuests =
                    questService.updateBattleWinProgress(character.getId());
                updatedBattle.setActiveQuests(updatedQuests);
            } catch (Exception e) {
                System.err.println("Erro ao atualizar progresso de quest: " + e.getMessage());
            }

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

    /**
     * Executa apenas o turno do monstro.
     */
    @Transactional
    public BattleStateResponse executeMonsterTurn(Integer userId) {
        BattleStateResponse battle = battleStateService.getActiveBattle(userId);
        if (battle == null) {
            throw new NotFoundException("Nenhuma batalha ativa encontrada.");
        }

        if (battle.getIsFinished()) {
            throw new BadRequestException("A batalha já foi finalizada.");
        }

        if (!Boolean.TRUE.equals(battle.getWaitingForMonsterTurn())) {
            throw new BadRequestException("Não está aguardando o turno do monstro.");
        }

        // 1. Executa o turno do monstro (ataque, defesa ou skill)
        CombatService.MonsterTurnResult monsterResult = combatService.performMonsterTurn(battle);
        battle.setMonsterDamageDealt(monsterResult.getDamageDealt());
        battle.setMonsterAction(monsterResult.getAction());
        String turnResult = monsterResult.getTurnResult();

        // 2. Aplica o dano pendente do jogador APÓS a ação do monstro
        if (battle.getPendingDamageToMonster() != null && battle.getPendingDamageToMonster() > 0) {
            int pendingDamage = battle.getPendingDamageToMonster();
            int finalDamage;

            // Verifica se é a Investida do Lutador
            boolean isCharge = battle.getCharacter().getEffects() != null &&
                             Boolean.TRUE.equals(battle.getCharacter().getEffects().get("isChargeActive"));

            if (isCharge) {
                // Investida: 125% se monstro NÃO defender, 115% se defender
                int amplifiedDamage;

                if (battle.getMonster().getIsDefending()) {
                    // Monstro defendendo: 115% de dano
                    amplifiedDamage = (pendingDamage * 115) / 100;

                    // Aplica considerando APENAS defesa base (não aplica bônus de 50% da defesa ativa)
                    finalDamage = combatService.calculateCharacterDamageWithDefenseAndEffects(
                        battle,
                        amplifiedDamage,
                        battle.getMonster().getDefense(),
                        false // NÃO aplica bônus de defesa ativa (Investida ignora)
                    );

                    turnResult = String.format("Sua Investida causou %d de dano mesmo através da defesa do monstro! ", finalDamage) + turnResult;
                } else {
                    // Monstro NÃO defendendo: 125% de dano
                    amplifiedDamage = (pendingDamage * 125) / 100;

                    // Aplica considerando apenas defesa base
                    finalDamage = combatService.calculateCharacterDamageWithDefenseAndEffects(
                        battle,
                        amplifiedDamage,
                        battle.getMonster().getDefense(),
                        false // Monstro não está defendendo
                    );

                    turnResult = String.format("Sua Investida causou %d de dano devastador! ", finalDamage) + turnResult;
                }

                // Remove efeito da Investida
                battle.getCharacter().getEffects().remove("isChargeActive");
                battle.getCharacter().getEffects().remove("chargeBaseDamage");

            } else {
                // Ataque normal
                finalDamage = combatService.calculateCharacterDamageWithDefenseAndEffects(
                    battle,
                    pendingDamage,
                    battle.getMonster().getDefense(),
                    battle.getMonster().getIsDefending()
                );

                // Adiciona informação no resultado
                if (finalDamage < pendingDamage) {
                    String mitigationInfo = String.format("Seu ataque causou %d de dano (de %d) devido à defesa do monstro! ",
                        finalDamage, pendingDamage);
                    turnResult = mitigationInfo + turnResult;
                } else {
                    String damageInfo = String.format("Seu ataque causou %d de dano! ", finalDamage);
                    turnResult = damageInfo + turnResult;
                }
            }

            // Aplica o dano final ao monstro
            battle.getMonster().setHp(battle.getMonster().getHp() - finalDamage);
            battle.setCharacterDamageDealt(finalDamage);

            // Limpa o dano pendente
            battle.setPendingDamageToMonster(0);
        }

        // 3. Reseta a defesa se nenhum dano foi causado
        if (monsterResult.getDamageDealt() == 0) {
            battle.getCharacter().setIsDefending(false);
        }
        battle.getMonster().setIsDefending(false);

        // 3.5. Atualiza os efeitos ativos (decrementa duração e remove expirados)
        combatService.updateActiveEffects(battle);

        // 4. Não está mais aguardando turno do monstro e devolve o turno ao jogador
        battle.setWaitingForMonsterTurn(false);
        battle.setIsPlayerTurn(true); // Devolve o turno ao jogador

        // 5. Verifica se o monstro foi derrotado (após aplicar o dano pendente)
        if (battle.getMonster().getHp() <= 0) {
            battle.setIsFinished(true);
            turnResult += " Você venceu a batalha!";

            // Busca as estatísticas do jogador
            PlayerStats stats = playerStatsRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Estatísticas não encontradas"));
            stats.setBattlesWon(stats.getBattlesWon() + 1);
            stats.setTotalXpEarned(stats.getTotalXpEarned() + gameConfig.getBattle().getXpWinReward());
            playerStatsRepository.save(stats);

            // Adiciona XP ao personagem
            characterRepository.findById(battle.getCharacter().getId()).ifPresent(ch -> {
                ch.setXp(ch.getXp() + gameConfig.getBattle().getXpWinReward());
                characterRepository.save(ch);
            });

            // Verifica level up
            CharacterService.LevelUpResult levelUpResult = characterService.checkForLevelUp(battle.getCharacter().getId());
            turnResult += " " + levelUpResult.getMessage();

            battleStateService.removeActiveBattle(userId);
        }
        // 6. Verifica se o personagem foi derrotado
        else if (battle.getCharacter().getHp() <= 0) {
            battle.setIsFinished(true);
            turnResult += " Você foi derrotado.";

            // Atualiza estatísticas
            PlayerStats stats = playerStatsRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Estatísticas não encontradas"));
            stats.setBattlesLost(stats.getBattlesLost() + 1);
            playerStatsRepository.save(stats);

            battleStateService.removeActiveBattle(userId);
        } else {
            // Batalha continua - persiste HP do personagem se foi alterado
            characterRepository.findById(battle.getCharacter().getId()).ifPresent(character -> {
                character.setHp(battle.getCharacter().getHp());
                characterRepository.save(character);
            });

            // Atualiza o estado da batalha na memória
            battleStateService.setActiveBattle(userId, battle);
        }

        battle.setTurnResult(turnResult);
        return battle;
    }

    /**
     * Permite que o jogador passe o turno quando está incapacitado (atordoado/STUN).
     * O monstro executa seu turno normalmente e os efeitos são atualizados.
     */
    @Transactional
    public BattleStateResponse skipTurn(Integer userId) {
        BattleStateResponse battle = battleStateService.getActiveBattle(userId);
        if (battle == null) {
            throw new NotFoundException("Nenhuma batalha ativa encontrada.");
        }

        if (battle.getIsFinished()) {
            throw new BadRequestException("A batalha já foi finalizada.");
        }

        if (!Boolean.TRUE.equals(battle.getIsPlayerTurn())) {
            throw new BadRequestException("Não é o seu turno!");
        }

        // Verifica se o jogador realmente está atordoado
        boolean isStunned = combatService.hasCharacterEffect(battle, "STUN");

        if (!isStunned) {
            throw new BadRequestException("Você só pode pular o turno quando estiver atordoado!");
        }

        // Marca que está aguardando turno do monstro
        battle.setWaitingForMonsterTurn(true);
        battle.setIsPlayerTurn(false);
        battle.setCharacterDamageDealt(0);
        battle.setMonsterDamageDealt(0);
        battle.setPendingDamageToMonster(0);

        String turnResult = "Você está atordoado e não consegue agir! O turno passou para o monstro.";
        battle.setTurnResult(turnResult);

        battleStateService.setActiveBattle(userId, battle);

        return battle;
    }
}

