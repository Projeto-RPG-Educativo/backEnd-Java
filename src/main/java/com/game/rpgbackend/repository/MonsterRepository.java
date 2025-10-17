package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Monster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonsterRepository extends JpaRepository<Monster, Integer> {
}
