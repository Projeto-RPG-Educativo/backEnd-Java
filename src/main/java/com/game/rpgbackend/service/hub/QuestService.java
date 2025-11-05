package com.game.rpgbackend.service.hub;

import com.game.rpgbackend.domain.*;
import com.game.rpgbackend.dto.response.hub.QuestDto;
import com.game.rpgbackend.enums.QuestType;
import com.game.rpgbackend.exception.BadRequestException;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela lógica de negócio das quests.
 * <p>
 * Gerencia a listagem, aceitação, progresso e conclusão de quests,
 * bem como a distribuição de recompensas.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final CharacterQuestRepository characterQuestRepository;
    private final com.game.rpgbackend.repository.CharacterRepository characterRepository;
    private final MonsterRepository monsterRepository;

    /**
     * Retorna todas as quests disponíveis no jogo.
     *
     * @return lista de todas as quests
     */
    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    /**
     * Retorna todas as quests disponíveis para um usuário.
     *
     * @param userId ID do usuário
     * @return lista de QuestDto com progresso
     */
    public List<QuestDto> getAvailableQuestsForUser(Integer userId) {
        List<com.game.rpgbackend.domain.Character> characters = characterRepository.findByUserId(userId);
        if (characters.isEmpty()) {
            throw new NotFoundException("Personagem não encontrado");
        }
        com.game.rpgbackend.domain.Character character = characters.get(0);
        return getAvailableQuestsForCharacter(character.getId());
    }

    /**
     * Retorna todas as quests disponíveis com informações de progresso do personagem.
     *
     * @param characterId ID do personagem
     * @return lista de QuestDto com progresso
     */
    public List<QuestDto> getAvailableQuestsForCharacter(Integer characterId) {
        List<Quest> allQuests = questRepository.findAll();
        List<CharacterQuest> characterQuests = characterQuestRepository.findByCharacterId(characterId);

        return allQuests.stream().map(quest -> {
            QuestDto dto = new QuestDto();
            dto.setId(quest.getId());
            dto.setTitle(quest.getTitle());
            dto.setDescription(quest.getDescription());
            dto.setXpReward(quest.getXpReward());
            dto.setGoldReward(quest.getGoldReward());
            dto.setType(quest.getType());
            dto.setTargetValue(quest.getTargetValue());
            dto.setTargetId(quest.getTargetId());

            // Adicionar nome do alvo se for DEFEAT_MONSTER
            if (quest.getType() == QuestType.DEFEAT_MONSTER && quest.getTargetId() != null) {
                monsterRepository.findById(quest.getTargetId()).ifPresent(monster ->
                    dto.setTargetName(monster.getMonsterName())
                );
            }

            // Verificar se o personagem já tem essa quest
            Optional<CharacterQuest> existingQuest = characterQuests.stream()
                .filter(cq -> cq.getQuestId().equals(quest.getId()))
                .findFirst();

            if (existingQuest.isPresent()) {
                dto.setProgress(existingQuest.get().getProgress());
                dto.setStatus(existingQuest.get().getStatus());
            } else {
                dto.setProgress(0);
                dto.setStatus(null);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Retorna as quests ativas por characterId (uso interno).
     *
     * @param characterId ID do personagem
     * @return lista de quests ativas
     */
    public List<QuestDto> getActiveQuestsByCharacterId(Integer characterId) {
        return getActiveQuests(characterId);
    }

    /**
     * Retorna as quests ativas de um usuário.
     *
     * @param userId ID do usuário
     * @return lista de quests ativas
     */
    public List<QuestDto> getActiveQuestsForUser(Integer userId) {
        List<com.game.rpgbackend.domain.Character> characters = characterRepository.findByUserId(userId);
        if (characters.isEmpty()) {
            throw new NotFoundException("Personagem não encontrado");
        }
        com.game.rpgbackend.domain.Character character = characters.get(0);
        return getActiveQuests(character.getId());
    }

    /**
     * Retorna as quests ativas (em progresso) de um personagem.
     *
     * @param characterId ID do personagem
     * @return lista de quests ativas
     */
    public List<QuestDto> getActiveQuests(Integer characterId) {
        System.out.println("DEBUG: Buscando quests ativas para characterId: " + characterId);

        List<CharacterQuest> allCharacterQuests = characterQuestRepository.findByCharacterId(characterId);
        System.out.println("DEBUG: Total de CharacterQuests encontrados: " + allCharacterQuests.size());

        List<CharacterQuest> activeQuests = allCharacterQuests.stream()
            .filter(cq -> {
                boolean isActive = "in_progress".equals(cq.getStatus());
                System.out.println("DEBUG: QuestId=" + cq.getQuestId() + ", Status=" + cq.getStatus() + ", IsActive=" + isActive);
                return isActive;
            })
            .collect(Collectors.toList());

        System.out.println("DEBUG: Quests ativas após filtro: " + activeQuests.size());

        return activeQuests.stream().map(cq -> {
            Quest quest = cq.getQuest();
            QuestDto dto = new QuestDto();
            dto.setId(quest.getId());
            dto.setTitle(quest.getTitle());
            dto.setDescription(quest.getDescription());
            dto.setXpReward(quest.getXpReward());
            dto.setGoldReward(quest.getGoldReward());
            dto.setType(quest.getType());
            dto.setTargetValue(quest.getTargetValue());
            dto.setTargetId(quest.getTargetId());
            dto.setProgress(cq.getProgress());
            dto.setStatus(cq.getStatus());

            if (quest.getType() == QuestType.DEFEAT_MONSTER && quest.getTargetId() != null) {
                monsterRepository.findById(quest.getTargetId()).ifPresent(monster ->
                    dto.setTargetName(monster.getMonsterName())
                );
            }

            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Aceita uma quest para um personagem específico.
     *
     * @param userId ID do usuário
     * @param questId ID da quest
     * @param characterId ID do personagem que está aceitando
     * @return AcceptQuestResponse com informações da quest aceita
     */
    @Transactional
    public com.game.rpgbackend.dto.response.hub.AcceptQuestResponse acceptQuest(Integer userId, Integer questId, Integer characterId) {
        // Busca o personagem e valida que pertence ao usuário
        com.game.rpgbackend.domain.Character character = characterRepository.findById(characterId)
            .orElseThrow(() -> new NotFoundException("Personagem não encontrado"));

        // Valida que o personagem pertence ao usuário
        if (!character.getUser().getId().equals(userId)) {
            throw new BadRequestException("Este personagem não pertence ao usuário autenticado");
        }

        Quest quest = questRepository.findById(questId)
            .orElseThrow(() -> new NotFoundException("Quest não encontrada"));

        // Verificar se o personagem já tem uma quest em progresso
        List<CharacterQuest> activeQuests = characterQuestRepository.findByCharacterId(character.getId())
            .stream()
            .filter(cq -> "in_progress".equals(cq.getStatus()))
            .collect(Collectors.toList());

        if (!activeQuests.isEmpty()) {
            throw new BadRequestException("Você já tem uma quest ativa. Complete-a antes de aceitar outra.");
        }

        // Verificar se já aceitou essa quest antes
        Optional<CharacterQuest> existingQuest = characterQuestRepository.findByCharacterId(character.getId())
            .stream()
            .filter(cq -> cq.getQuestId().equals(questId))
            .findFirst();

        if (existingQuest.isPresent()) {
            if ("completed".equals(existingQuest.get().getStatus())) {
                throw new BadRequestException("Você já completou essa quest.");
            }
            if ("in_progress".equals(existingQuest.get().getStatus())) {
                throw new BadRequestException("Você já aceitou essa quest.");
            }
        }

        // Criar nova CharacterQuest
        CharacterQuest characterQuest = new CharacterQuest();
        characterQuest.setCharacterId(character.getId());
        characterQuest.setQuestId(questId);
        characterQuest.setCharacter(character);
        characterQuest.setQuest(quest);
        characterQuest.setStatus("in_progress");
        characterQuest.setProgress(0);

        characterQuestRepository.save(characterQuest);

        // Criar resposta sem referências circulares
        com.game.rpgbackend.dto.response.hub.AcceptQuestResponse response =
            new com.game.rpgbackend.dto.response.hub.AcceptQuestResponse();
        response.setCharacterId(character.getId());
        response.setCharacterName(character.getName());
        response.setQuestId(quest.getId());
        response.setQuestTitle(quest.getTitle());
        response.setQuestDescription(quest.getDescription());
        response.setStatus("in_progress");
        response.setProgress(0);
        response.setTargetValue(quest.getTargetValue());
        response.setMessage("Quest aceita com sucesso! Boa sorte na sua jornada!");

        return response;
    }

    /**
     * Atualiza o progresso de uma quest de responder perguntas.
     *
     * @param characterId ID do personagem
     * @param questId ID da quest
     */
    @Transactional
    public void updateQuestionProgress(Integer characterId, Integer questId) {
        CharacterQuest characterQuest = getActiveCharacterQuest(characterId, questId);

        characterQuest.setProgress(characterQuest.getProgress() + 1);

        // Verificar se completou a quest
        Quest quest = characterQuest.getQuest();
        if (characterQuest.getProgress() >= quest.getTargetValue()) {
            completeQuest(characterQuest);
        } else {
            characterQuestRepository.save(characterQuest);
        }
    }

    /**
     * Atualiza o progresso de todas as quests ativas de ANSWER_QUESTIONS e retorna todas as quests ativas.
     *
     * @param characterId ID do personagem
     * @return Lista de todas as quests ativas com progresso atualizado
     */
    @Transactional
    public List<QuestDto> updateQuestionProgressForAllActiveQuests(Integer characterId) {
        // Busca quests ativas de ANSWER_QUESTIONS
        List<CharacterQuest> answerQuests = characterQuestRepository.findByCharacterId(characterId)
            .stream()
            .filter(cq -> "in_progress".equals(cq.getStatus()))
            .filter(cq -> cq.getQuest().getType() == QuestType.ANSWER_QUESTIONS)
            .collect(Collectors.toList());

        // Atualiza progresso das quests de perguntas
        for (CharacterQuest characterQuest : answerQuests) {
            characterQuest.setProgress(characterQuest.getProgress() + 1);

            Quest quest = characterQuest.getQuest();
            if (characterQuest.getProgress() >= quest.getTargetValue()) {
                completeQuest(characterQuest);
            } else {
                characterQuestRepository.save(characterQuest);
            }
        }

        // Retorna TODAS as quests ativas (atualizadas)
        return getActiveQuests(characterId);
    }

    /**
     * Atualiza progresso quando o personagem derrota um monstro e retorna todas as quests ativas.
     *
     * @param characterId ID do personagem
     * @param monsterId ID do monstro derrotado
     * @return Lista de todas as quests ativas com progresso atualizado
     */
    @Transactional
    public List<QuestDto> updateMonsterDefeatProgress(Integer characterId, Integer monsterId) {
        // Buscar quests ativas de derrotar monstro
        List<CharacterQuest> monsterQuests = characterQuestRepository.findByCharacterId(characterId)
            .stream()
            .filter(cq -> "in_progress".equals(cq.getStatus()))
            .filter(cq -> cq.getQuest().getType() == QuestType.DEFEAT_MONSTER)
            .filter(cq -> cq.getQuest().getTargetId() != null && cq.getQuest().getTargetId().equals(monsterId))
            .collect(Collectors.toList());

        // Atualiza progresso das quests de derrotar monstro
        for (CharacterQuest characterQuest : monsterQuests) {
            characterQuest.setProgress(characterQuest.getProgress() + 1);

            Quest quest = characterQuest.getQuest();
            if (characterQuest.getProgress() >= quest.getTargetValue()) {
                completeQuest(characterQuest);
            } else {
                characterQuestRepository.save(characterQuest);
            }
        }

        // Retorna TODAS as quests ativas (atualizadas)
        return getActiveQuests(characterId);
    }

    /**
     * Atualiza progresso quando o personagem vence uma batalha e retorna todas as quests ativas.
     *
     * @param characterId ID do personagem
     * @return Lista de todas as quests ativas com progresso atualizado
     */
    @Transactional
    public List<QuestDto> updateBattleWinProgress(Integer characterId) {
        // Buscar quests ativas de vencer batalhas
        List<CharacterQuest> winQuests = characterQuestRepository.findByCharacterId(characterId)
            .stream()
            .filter(cq -> "in_progress".equals(cq.getStatus()))
            .filter(cq -> cq.getQuest().getType() == QuestType.WIN_BATTLES)
            .collect(Collectors.toList());

        // Atualiza progresso das quests de vencer batalhas
        for (CharacterQuest characterQuest : winQuests) {
            characterQuest.setProgress(characterQuest.getProgress() + 1);

            Quest quest = characterQuest.getQuest();
            if (characterQuest.getProgress() >= quest.getTargetValue()) {
                completeQuest(characterQuest);
            } else {
                characterQuestRepository.save(characterQuest);
            }
        }

        // Retorna TODAS as quests ativas (atualizadas)
        return getActiveQuests(characterId);
    }

    /**
     * Completa uma quest e distribui recompensas.
     *
     * @param characterQuest quest a ser completada
     */
    protected void completeQuest(CharacterQuest characterQuest) {
        characterQuest.setStatus("completed");
        characterQuestRepository.save(characterQuest);

        // Distribuir recompensas
        com.game.rpgbackend.domain.Character character = characterQuest.getCharacter();
        Quest quest = characterQuest.getQuest();

        if (quest.getXpReward() != null && quest.getXpReward() > 0) {
            character.setXp(character.getXp() + quest.getXpReward());
        }

        if (quest.getGoldReward() != null && quest.getGoldReward() > 0) {
            character.setGold(character.getGold() + quest.getGoldReward());
        }

        characterRepository.save(character);
    }

    /**
     * Cria um DTO com informações de progresso da quest.
     *
     * @param characterQuest quest atualizada
     * @param justCompleted se a quest acabou de ser completada
     * @return DTO com informações de progresso
     */
    private com.game.rpgbackend.dto.response.battle.QuestProgressDto createQuestProgressDto(
            CharacterQuest characterQuest, boolean justCompleted) {

        Quest quest = characterQuest.getQuest();
        com.game.rpgbackend.dto.response.battle.QuestProgressDto dto =
            new com.game.rpgbackend.dto.response.battle.QuestProgressDto();

        dto.setQuestId(quest.getId());
        dto.setQuestTitle(quest.getTitle());
        dto.setQuestType(quest.getType());
        dto.setCurrentProgress(characterQuest.getProgress());
        dto.setTargetValue(quest.getTargetValue());
        dto.setJustCompleted(justCompleted);

        // Criar mensagem de progresso
        String message;
        if (justCompleted) {
            message = String.format("Quest completada! Você recebeu %d XP e %d Gold!",
                quest.getXpReward(), quest.getGoldReward());
        } else {
            message = String.format("Progresso: %d/%d %s",
                characterQuest.getProgress(),
                quest.getTargetValue(),
                getProgressTypeMessage(quest.getType()));
        }
        dto.setProgressMessage(message);

        return dto;
    }

    /**
     * Retorna mensagem descritiva do tipo de progresso.
     */
    private String getProgressTypeMessage(QuestType type) {
        switch (type) {
            case ANSWER_QUESTIONS:
                return "perguntas acertadas";
            case DEFEAT_MONSTER:
                return "vezes derrotado";
            case WIN_BATTLES:
                return "batalhas vencidas";
            case REACH_LEVEL:
                return "nível atual";
            default:
                return "";
        }
    }

    /**
     * Busca uma quest ativa de um personagem.
     *
     * @param characterId ID do personagem
     * @param questId ID da quest
     * @return CharacterQuest encontrado
     */
    private CharacterQuest getActiveCharacterQuest(Integer characterId, Integer questId) {
        return characterQuestRepository.findByCharacterId(characterId)
            .stream()
            .filter(cq -> cq.getQuestId().equals(questId))
            .filter(cq -> "in_progress".equals(cq.getStatus()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Quest ativa não encontrada"));
    }

    /**
     * Abandona uma quest ativa de um personagem específico.
     *
     * @param userId ID do usuário
     * @param questId ID da quest
     * @param characterId ID do personagem
     */
    @Transactional
    public void abandonQuest(Integer userId, Integer questId, Integer characterId) {
        // Busca o personagem e valida que pertence ao usuário
        com.game.rpgbackend.domain.Character character = characterRepository.findById(characterId)
            .orElseThrow(() -> new NotFoundException("Personagem não encontrado"));

        // Valida que o personagem pertence ao usuário
        if (!character.getUser().getId().equals(userId)) {
            throw new BadRequestException("Este personagem não pertence ao usuário autenticado");
        }

        CharacterQuest characterQuest = getActiveCharacterQuest(character.getId(), questId);
        characterQuest.setStatus("failed");
        characterQuest.setProgress(0);
        characterQuestRepository.save(characterQuest);
    }
}

