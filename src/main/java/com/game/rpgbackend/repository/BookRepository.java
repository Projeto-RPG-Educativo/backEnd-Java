package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência de livros da Biblioteca Silenciosa.
 * <p>
 * Gerencia o acesso aos livros educacionais disponíveis no Hub,
 * permitindo filtrar por tipo e dificuldade de leitura.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    /**
     * Busca todos os livros de um tipo específico.
     *
     * @param type tipo do livro (ficção, técnico, gramática, histórico)
     * @return lista de livros do tipo especificado
     */
    List<Book> findByType(String type);

    /**
     * Busca todos os livros de uma dificuldade específica.
     *
     * @param difficulty nível de dificuldade (easy, medium, hard)
     * @return lista de livros da dificuldade especificada
     */
    List<Book> findByDifficulty(String difficulty);
}
