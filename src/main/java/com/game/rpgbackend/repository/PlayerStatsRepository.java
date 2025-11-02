package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório para operações de persistência de estatísticas de jogadores.
 * <p>
 * Gerencia o acesso às estatísticas e progressão dos jogadores,
 * incluindo nível, XP, batalhas, questões e habilidades desbloqueadas.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Integer> {

    /**
     * Busca as estatísticas de um usuário específico.
     * Cada usuário possui um único registro de estatísticas.
     *
     * @param userId identificador do usuário
     * @return Optional contendo as estatísticas se encontradas
     */
    Optional<PlayerStats> findByUserId(Integer userId);
}
