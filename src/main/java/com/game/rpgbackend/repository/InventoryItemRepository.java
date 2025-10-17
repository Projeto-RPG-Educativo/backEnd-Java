package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {
    List<InventoryItem> findByInventoryId(Integer inventoryId);
    List<InventoryItem> findByItemId(Integer itemId);
}
