package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório para operações de persistência de inventários de personagens.
 * <p>
 * Gerencia o acesso aos inventários únicos de cada personagem,
 * onde são armazenados todos os itens coletados e comprados.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    /**
     * Busca o inventário de um personagem específico.
     * Cada personagem possui um único inventário.
     *
     * @param characterId identificador do personagem
     * @return Optional contendo o inventário se encontrado
     */
    Optional<Inventory> findByCharacterId(Integer characterId);
}
