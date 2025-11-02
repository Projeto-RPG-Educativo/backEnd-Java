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
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    /**
     * Busca todas as questões de um conteúdo educacional específico.
     *
     * @param contentId identificador do conteúdo
     * @return lista de questões do conteúdo
     */
    List<Question> findByContentId(Integer contentId);

    /**
     * Busca questões por nível de dificuldade.
     *
     * @param difficulty nível (easy, medium, hard)
     * @return lista de questões da dificuldade especificada
     */
    List<Question> findByDifficulty(String difficulty);

    /**
     * Busca questões por categoria de conteúdo.
     *
     * @param questionContent categoria da questão
     * @return lista de questões da categoria
     */
    List<Question> findByQuestionContent(String questionContent);

    /**
     * Busca questões acessíveis para um determinado nível de jogador.
     *
     * @param level nível do jogador
     * @return lista de questões com minLevel menor ou igual ao nível fornecido
     */
    List<Question> findByMinLevelLessThanEqual(Integer level);

    /**
     * Conta questões de uma dificuldade acessíveis para um nível.
     *
     * @param difficulty nível de dificuldade
     * @param level nível do jogador
     * @return quantidade de questões que atendem os critérios
     */
    long countByDifficultyAndMinLevelLessThanEqual(String difficulty, Integer level);

    /**
     * Conta questões de uma dificuldade, nível e conteúdo específicos.
     *
     * @param difficulty nível de dificuldade
     * @param level nível do jogador
     * @param contentId identificador do conteúdo
     * @return quantidade de questões que atendem os critérios
     */
    long countByDifficultyAndMinLevelLessThanEqualAndContentId(String difficulty, Integer level, Integer contentId);

    /**
     * Busca questões de uma dificuldade acessíveis para um nível.
     *
     * @param difficulty nível de dificuldade
     * @param level nível do jogador
     * @return lista de questões que atendem os critérios
     */
    List<Question> findByDifficultyAndMinLevelLessThanEqual(String difficulty, Integer level);

    /**
     * Busca questões de uma dificuldade, nível e conteúdo específicos.
     *
     * @param difficulty nível de dificuldade
     * @param level nível do jogador
     * @param contentId identificador do conteúdo
     * @return lista de questões que atendem os critérios
     */
    List<Question> findByDifficultyAndMinLevelLessThanEqualAndContentId(String difficulty, Integer level, Integer contentId);

    /**
     * Conta questões de múltiplas dificuldades acessíveis para um nível.
     *
     * @param difficulties lista de níveis de dificuldade
     * @param level nível do jogador
     * @return quantidade de questões que atendem os critérios
     */
    long countByDifficultyInAndMinLevelLessThanEqual(List<String> difficulties, Integer level);

    /**
     * Busca questões de múltiplas dificuldades acessíveis para um nível.
     *
     * @param difficulties lista de níveis de dificuldade
     * @param level nível do jogador
     * @return lista de questões que atendem os critérios
     */
    List<Question> findByDifficultyInAndMinLevelLessThanEqual(List<String> difficulties, Integer level);
}
