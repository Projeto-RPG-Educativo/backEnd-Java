package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.ItemLoja;
import com.game.rpgbackend.domain.ItemLojaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemLojaRepository extends JpaRepository<ItemLoja, ItemLojaId> {
    List<ItemLoja> findByLojaId(Integer lojaId);
    List<ItemLoja> findByItemId(Integer itemId);
}
