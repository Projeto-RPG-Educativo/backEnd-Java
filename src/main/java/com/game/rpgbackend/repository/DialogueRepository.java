package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Dialogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DialogueRepository extends JpaRepository<Dialogue, Integer> {
    List<Dialogue> findByNpcId(Integer npcId);
}
