package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.BattleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência do histórico de batalhas.
 * <p>
 * Gerencia o registro de todas as batalhas realizadas pelos jogadores,
 * incluindo resultados, inimigos enfrentados e experiência ganha.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface BattleHistoryRepository extends JpaRepository<BattleHistory, Integer> {

    /**
     * Busca todo o histórico de batalhas de um usuário específico.
     *
     * @param userId identificador do usuário
     * @return lista de batalhas realizadas pelo usuário
     */
    List<BattleHistory> findByUserId(Integer userId);

    /**
     * Busca batalhas por resultado específico.
     *
     * @param result resultado da batalha (vitória, derrota)
     * @return lista de batalhas com o resultado especificado
     */
    List<BattleHistory> findByResult(String result);
}
