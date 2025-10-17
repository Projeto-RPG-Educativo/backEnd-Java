package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    Optional<Content> findByNome(String nome);
    List<Content> findByLevelMinimoLessThanEqual(Integer level);
}
