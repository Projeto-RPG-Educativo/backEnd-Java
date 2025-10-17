package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Integer> {
    Optional<Quest> findByTitle(String title);
}
