package com.game.rpgbackend.service.hub;

import com.game.rpgbackend.domain.Book;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela Biblioteca Silenciosa (Books).
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibraryService {

    private final BookRepository bookRepository;

    /**
     * Retorna todos os livros disponíveis.
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findAll();
    }

    /**
     * Busca um livro específico por ID.
     */
    public Book getBookById(Integer bookId) {
        return bookRepository.findById(bookId)
            .orElseThrow(() -> new NotFoundException("Livro não encontrado"));
    }

    /**
     * Busca livros por tipo.
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

