package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.NPC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface NPCRepository extends JpaRepository<NPC, Integer> {
    Optional<NPC> findByName(String name);
    List<NPC> findByType(String type);
    List<NPC> findByLocation(String location);
}
