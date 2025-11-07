package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Achievement;
import com.game.rpgbackend.enums.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para gerenciar operações de persistência da entidade {@link Achievement}.
 * <p>
 * Fornece métodos para consultar conquistas por personagem, tipo, status de conclusão,
 * e verificar existência de conquistas específicas.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 2.0
 * @since 1.0
 */
@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    /**
     * Busca todas as conquistas de um personagem específico.
     *
     * @param characterId ID do personagem
     * @return lista de conquistas do personagem
     */
    List<Achievement> findByCharacterId(Long characterId);

    /**
     * Busca apenas as conquistas completadas de um personagem.
     *
     * @param characterId ID do personagem
     * @return lista de conquistas completadas
     */
    List<Achievement> findByCharacterIdAndIsCompletedTrue(Long characterId);

    /**
     * Busca apenas as conquistas em progresso (não completadas) de um personagem.
     *
     * @param characterId ID do personagem
     * @return lista de conquistas em progresso
     */
    List<Achievement> findByCharacterIdAndIsCompletedFalse(Long characterId);

    /**
     * Busca uma conquista específica de um personagem pelo tipo.
     *
     * @param characterId ID do personagem
     * @param type tipo da conquista
     * @return Optional contendo a conquista se existir
     */
    Optional<Achievement> findByCharacterIdAndType(Long characterId, AchievementType type);

    /**
     * Verifica se um personagem possui uma conquista específica.
     *
     * @param characterId ID do personagem
     * @param type tipo da conquista
     * @return true se a conquista existir, false caso contrário
     */
    boolean existsByCharacterIdAndType(Long characterId, AchievementType type);

    /**
     * Conta quantas conquistas completadas um personagem possui.
     *
     * @param characterId ID do personagem
     * @return número de conquistas completadas
     */
    @Query("SELECT COUNT(a) FROM Achievement a WHERE a.character.id = :characterId AND a.isCompleted = true")
    long countCompletedAchievements(@Param("characterId") Long characterId);

    /**
     * Busca todas as conquistas de um personagem ordenadas por data de desbloqueio.
     *
     * @param characterId ID do personagem
     * @return lista de conquistas ordenadas por data de desbloqueio (mais recente primeiro)
     */
    @Query("SELECT a FROM Achievement a WHERE a.character.id = :characterId AND a.isCompleted = true ORDER BY a.unlockedAt DESC")
    List<Achievement> findRecentlyUnlockedAchievements(@Param("characterId") Long characterId);
}
