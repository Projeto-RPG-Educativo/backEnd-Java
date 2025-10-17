package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.GameSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameSaveRepository extends JpaRepository<GameSave, Integer> {
    List<GameSave> findByUserId(Integer userId);
    Optional<GameSave> findByUserIdAndSlotName(Integer userId, String slotName);
}
