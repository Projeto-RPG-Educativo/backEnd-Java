package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um livro no jogo.
 * <p>
 * Livros são itens de leitura que podem conter histórias, lore do jogo
 * ou conteúdo educacional. Possuem tipo e dificuldade associados.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    /** Identificador único do livro */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Título do livro */
    @Column(nullable = false)
    private String bookTitle;

    /** Conteúdo/texto completo do livro */
    @Column(nullable = false)
    private String content;

    /** Tipo/categoria do livro (ficção, histórico, técnico, etc.) */
    @Column(nullable = false)
    private String type;

    /** Nível de dificuldade de leitura (easy, medium, hard) */
    @Column(nullable = false)
    private String difficulty;
}
