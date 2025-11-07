package com.game.rpgbackend.service.achievement;

import com.game.rpgbackend.domain.Achievement;
import com.game.rpgbackend.domain.Character;
import com.game.rpgbackend.enums.AchievementType;
import com.game.rpgbackend.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar as conquistas (achievements) dos personagens.
 * <p>
 * Fornece funcionalidades para:
 * <ul>
 *   <li>Inicializar conquistas para novos personagens</li>
 *   <li>Atualizar progresso de conquistas específicas</li>
 *   <li>Verificar e completar conquistas automaticamente</li>
 *   <li>Consultar conquistas por personagem</li>
 *   <li>Calcular estatísticas de conquistas</li>
 * </ul>
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final AchievementRepository achievementRepository;

    /**
     * Inicializa todas as conquistas para um novo personagem.
     * <p>
     * Cria um registro de Achievement para cada tipo de conquista definido
     * no enum {@link AchievementType}, com progresso inicial zerado.
     * </p>
     *
     * @param character personagem para inicializar as conquistas
     */
    @Transactional
    public void initializeAchievementsForCharacter(Character character) {
        log.info("Inicializando conquistas para o personagem ID: {}", character.getId());

        List<Achievement> achievements = new ArrayList<>();

        for (AchievementType type : AchievementType.values()) {
            if (!achievementRepository.existsByCharacterIdAndType(character.getId().longValue(), type)) {
                Achievement achievement = new Achievement();
                achievement.setCharacter(character);
                achievement.setType(type);
                achievement.setProgress(0);
                achievement.setCompleted(false);
                achievements.add(achievement);
            }
        }

        achievementRepository.saveAll(achievements);
        log.info("Inicializadas {} conquistas para o personagem ID: {}",
                achievements.size(), character.getId());
    }

    /**
     * Atualiza o progresso de uma conquista específica.
     * <p>
     * Se a conquista não existir para o personagem, ela será criada.
     * Verifica automaticamente se a conquista foi completada após o incremento.
     * </p>
     *
     * @param characterId ID do personagem
     * @param achievementType tipo da conquista a ser atualizada
     * @param increment valor a ser adicionado ao progresso
     * @return true se a conquista foi completada com esta atualização, false caso contrário
     */
    @Transactional
    public boolean updateAchievementProgress(Long characterId,
                                             AchievementType achievementType,
                                             int increment) {
        log.debug("Atualizando progresso da conquista {} para personagem ID: {}",
                achievementType, characterId);

        Achievement achievement = achievementRepository
                .findByCharacterIdAndType(characterId, achievementType)
                .orElseGet(() -> createAchievement(characterId, achievementType));

        boolean wasCompleted = achievement.addProgress(increment);
        achievementRepository.save(achievement);

        if (wasCompleted) {
            log.info("Conquista {} desbloqueada pelo personagem ID: {}",
                    achievementType.getName(), characterId);
        }

        return wasCompleted;
    }

    /**
     * Atualiza o progresso de múltiplas conquistas de uma vez.
     * <p>
     * Útil para situações onde uma ação pode desbloquear várias conquistas
     * simultaneamente (ex: vitória em batalha pode contar para múltiplas conquistas).
     * </p>
     *
     * @param characterId ID do personagem
     * @param progressUpdates array de objetos contendo tipo e incremento
     * @return lista de conquistas que foram completadas nesta atualização
     */
    @Transactional
    public List<Achievement> updateMultipleAchievements(Long characterId,
                                                        ProgressUpdate... progressUpdates) {
        List<Achievement> completedAchievements = new ArrayList<>();

        for (ProgressUpdate update : progressUpdates) {
            boolean wasCompleted = updateAchievementProgress(
                    characterId,
                    update.getType(),
                    update.getIncrement()
            );

            if (wasCompleted) {
                achievementRepository.findByCharacterIdAndType(characterId, update.getType())
                        .ifPresent(completedAchievements::add);
            }
        }

        return completedAchievements;
    }

    /**
     * Busca todas as conquistas de um personagem.
     *
     * @param characterId ID do personagem
     * @return lista de todas as conquistas do personagem
     */
    @Transactional(readOnly = true)
    public List<Achievement> getCharacterAchievements(Long characterId) {
        return achievementRepository.findByCharacterId(characterId);
    }

    /**
     * Busca apenas as conquistas completadas de um personagem.
     *
     * @param characterId ID do personagem
     * @return lista de conquistas completadas
     */
    @Transactional(readOnly = true)
    public List<Achievement> getCompletedAchievements(Long characterId) {
        return achievementRepository.findByCharacterIdAndIsCompletedTrue(characterId);
    }

    /**
     * Busca apenas as conquistas em progresso de um personagem.
     *
     * @param characterId ID do personagem
     * @return lista de conquistas em progresso
     */
    @Transactional(readOnly = true)
    public List<Achievement> getInProgressAchievements(Long characterId) {
        return achievementRepository.findByCharacterIdAndIsCompletedFalse(characterId);
    }

    /**
     * Busca as conquistas desbloqueadas mais recentemente.
     *
     * @param characterId ID do personagem
     * @return lista de conquistas ordenadas por data de desbloqueio
     */
    @Transactional(readOnly = true)
    public List<Achievement> getRecentlyUnlockedAchievements(Long characterId) {
        return achievementRepository.findRecentlyUnlockedAchievements(characterId);
    }

    /**
     * Retorna o percentual de conclusão de todas as conquistas de um personagem.
     *
     * @param characterId ID do personagem
     * @return percentual de conquistas completadas (0-100)
     */
    @Transactional(readOnly = true)
    public double getCompletionPercentage(Long characterId) {
        long totalAchievements = AchievementType.values().length;
        long completedAchievements = achievementRepository.countCompletedAchievements(characterId);

        if (totalAchievements == 0) {
            return 0.0;
        }

        return (completedAchievements * 100.0) / totalAchievements;
    }

    /**
     * Verifica se um personagem possui uma conquista específica.
     *
     * @param characterId ID do personagem
     * @param achievementType tipo da conquista
     * @return Optional contendo a conquista se existir
     */
    @Transactional(readOnly = true)
    public Optional<Achievement> getAchievement(Long characterId, AchievementType achievementType) {
        return achievementRepository.findByCharacterIdAndType(characterId, achievementType);
    }

    /**
     * Cria uma nova conquista para um personagem.
     *
     * @param characterId ID do personagem
     * @param achievementType tipo da conquista
     * @return conquista criada
     */
    private Achievement createAchievement(Long characterId, AchievementType achievementType) {
        Achievement achievement = new Achievement();
        Character character = new Character();
        character.setId(characterId.intValue());
        achievement.setCharacter(character);
        achievement.setType(achievementType);
        achievement.setProgress(0);
        achievement.setCompleted(false);

        return achievementRepository.save(achievement);
    }

    /**
     * Classe auxiliar para agrupar atualizações de progresso.
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class ProgressUpdate {
        private AchievementType type;
        private int increment;
    }
}
