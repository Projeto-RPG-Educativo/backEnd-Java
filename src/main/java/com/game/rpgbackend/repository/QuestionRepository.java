package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência da entidade Question.
 * <p>
 * Fornece métodos de consulta para buscar questões por dificuldade,
 * nível mínimo, conteúdo e combinações destes critérios.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    /**
     * Busca questões por ID do conteúdo.
     */
    List<Question> findByContentId(Integer contentId);

    /**
     * Busca questões por dificuldade.
     */
    List<Question> findByDifficulty(String difficulty);

    /**
     * Busca questões por categoria de conteúdo.
     */
    List<Question> findByQuestionContent(String questionContent);

    /**
     * Busca questões com nível mínimo menor ou igual ao especificado.
     */
    List<Question> findByMinLevelLessThanEqual(Integer level);

    /**
     * Conta questões por dificuldade e nível mínimo.
     */
    long countByDifficultyAndMinLevelLessThanEqual(String difficulty, Integer level);

    /**
     * Conta questões por dificuldade, nível e conteúdo.
     */
    long countByDifficultyAndMinLevelLessThanEqualAndContentId(String difficulty, Integer level, Integer contentId);

    /**
     * Busca questões por dificuldade e nível mínimo.
     */
    List<Question> findByDifficultyAndMinLevelLessThanEqual(String difficulty, Integer level);

    /**
     * Busca questões por dificuldade, nível e conteúdo.
     */
    List<Question> findByDifficultyAndMinLevelLessThanEqualAndContentId(String difficulty, Integer level, Integer contentId);

    /**
     * Conta questões com múltiplas dificuldades e nível.
     */
    long countByDifficultyInAndMinLevelLessThanEqual(List<String> difficulties, Integer level);

    /**
     * Busca questões com múltiplas dificuldades e nível.
     */
    List<Question> findByDifficultyInAndMinLevelLessThanEqual(List<String> difficulties, Integer level);
}
