package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.GameSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência de salvamentos de jogo.
 * <p>
 * Gerencia os saves dos jogadores, permitindo criar múltiplos slots
 * de salvamento e carregar o progresso de partidas anteriores.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface GameSaveRepository extends JpaRepository<GameSave, Integer> {

    /**
     * Busca todos os salvamentos de um usuário específico.
     *
     * @param userId identificador do usuário
     * @return lista de salvamentos do usuário
     */
    List<GameSave> findByUserId(Integer userId);

    /**
     * Busca um salvamento específico por usuário e slot.
     *
     * @param userId identificador do usuário
     * @param slotName nome do slot (slot1, slot2, etc.)
     * @return Optional contendo o salvamento se encontrado
     */
    Optional<GameSave> findByUserIdAndSlotName(Integer userId, String slotName);
}
