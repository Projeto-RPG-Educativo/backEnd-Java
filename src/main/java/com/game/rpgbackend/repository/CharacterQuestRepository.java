package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.CharacterQuest;
import com.game.rpgbackend.domain.CharacterQuestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CharacterQuestRepository extends JpaRepository<CharacterQuest, CharacterQuestId> {
    List<CharacterQuest> findByCharacterId(Integer characterId);
    List<CharacterQuest> findByQuestId(Integer questId);
    List<CharacterQuest> findByStatus(String status);
}
