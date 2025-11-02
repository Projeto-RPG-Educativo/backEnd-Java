package com.game.rpgbackend.service.hub;

import com.game.rpgbackend.domain.Book;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela gestão da Biblioteca Silenciosa.
 * <p>
 * A Biblioteca é um local no Hub onde jogadores podem acessar livros
 * educacionais sobre gramática inglesa, lore do jogo e conteúdo didático.
 * Os livros são organizados por tipo e dificuldade.
 * </p>
 * <p>
 * Funcionalidades:
 * - Listar todos os livros disponíveis
 * - Buscar livro específico por ID
 * - Filtrar livros por tipo ou dificuldade
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibraryService {

    private final BookRepository bookRepository;

    /**
     * Retorna todos os livros disponíveis na biblioteca.
     * <p>
     * Inclui livros de todos os tipos e dificuldades.
     * </p>
     *
     * @return lista completa de livros disponíveis
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findAll();
    }

    /**
     * Busca um livro específico por seu identificador.
     *
     * @param bookId identificador único do livro
     * @return livro encontrado com conteúdo completo
     * @throws NotFoundException se o livro não for encontrado
     */
    public Book getBookById(Integer bookId) {
        return bookRepository.findById(bookId)
            .orElseThrow(() -> new NotFoundException("Livro não encontrado"));
    }

    /**
     * Busca livros por categoria/tipo.
     * <p>
     * Tipos possíveis: ficção, técnico, histórico, gramática, etc.
     * </p>
     *
     * @param type categoria do livro
     * @return lista de livros do tipo especificado
     */
    public List<Book> getBooksByType(String type) {
        return bookRepository.findByType(type);
    }

    /**
     * Busca livros por dificuldade.
     */
    public List<Book> getBooksByDifficulty(String difficulty) {
        return bookRepository.findByDifficulty(difficulty);
    }
}

