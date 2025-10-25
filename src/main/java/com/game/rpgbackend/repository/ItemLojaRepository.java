package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.ItemStore;
import com.game.rpgbackend.domain.ItemStoreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemLojaRepository extends JpaRepository<ItemStore, ItemStoreId> {
    List<ItemStore> findByStoreId(Integer storeId);
    List<ItemStore> findByItemId(Integer itemId);
}
