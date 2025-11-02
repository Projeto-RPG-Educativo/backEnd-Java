package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência de itens em inventários.
 * <p>
 * Gerencia a relação many-to-many entre inventários e itens,
 * incluindo as quantidades de cada item em cada inventário.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {

    /**
     * Busca todos os itens de um inventário específico.
     *
     * @param inventoryId identificador do inventário
     * @return lista de itens no inventário
     */
    List<InventoryItem> findByInventoryId(Integer inventoryId);

    /**
     * Busca todos os inventários que contêm um item específico.
     *
     * @param itemId identificador do item
     * @return lista de relações inventário-item
     */
    List<InventoryItem> findByItemId(Integer itemId);
}
