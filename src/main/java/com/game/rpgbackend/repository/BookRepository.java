package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByType(String type);
    List<Book> findByDifficulty(String difficulty);
}
