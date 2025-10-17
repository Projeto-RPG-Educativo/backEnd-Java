package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.QuestRewardItem;
import com.game.rpgbackend.domain.QuestRewardItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestRewardItemRepository extends JpaRepository<QuestRewardItem, QuestRewardItemId> {
    List<QuestRewardItem> findByQuestId(Integer questId);
    List<QuestRewardItem> findByItemId(Integer itemId);
}
