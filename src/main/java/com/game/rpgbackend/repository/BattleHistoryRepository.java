package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.BattleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BattleHistoryRepository extends JpaRepository<BattleHistory, Integer> {
    List<BattleHistory> findByUserId(Integer userId);
    List<BattleHistory> findByResult(String result);
}
