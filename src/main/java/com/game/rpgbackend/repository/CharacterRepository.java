package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {
    List<Character> findByUserId(Integer userId);
    List<Character> findByUserIdOrderByIdDesc(Integer userId);
}
