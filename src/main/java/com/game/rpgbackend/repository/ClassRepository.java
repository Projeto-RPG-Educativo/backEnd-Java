package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.GameClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<GameClass, Integer> {
    Optional<GameClass> findByName(String name);
}
