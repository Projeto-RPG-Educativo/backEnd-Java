package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Integer> {
    Optional<PlayerStats> findByUserId(Integer userId);
}
