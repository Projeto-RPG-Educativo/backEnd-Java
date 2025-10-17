package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByContentId(Integer contentId);
    List<Question> findByDificuldade(String dificuldade);
    List<Question> findByConteudo(String conteudo);
    List<Question> findByLevelMinimoLessThanEqual(Integer level);

    // Métodos adicionais para o serviço de batalha
    long countByDificuldadeAndLevelMinimoLessThanEqual(String dificuldade, Integer level);
    long countByDificuldadeAndLevelMinimoLessThanEqualAndContentId(String dificuldade, Integer level, Integer contentId);

    List<Question> findByDificuldadeAndLevelMinimoLessThanEqual(String dificuldade, Integer level);
    List<Question> findByDificuldadeAndLevelMinimoLessThanEqualAndContentId(String dificuldade, Integer level, Integer contentId);

    long countByDificuldadeInAndLevelMinimoLessThanEqual(List<String> dificuldades, Integer level);
    List<Question> findByDificuldadeInAndLevelMinimoLessThanEqual(List<String> dificuldades, Integer level);
}
