package com.game.rpgbackend.dto.response.hub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para livros da Biblioteca Silenciosa.
 * <p>
 * Representa um livro educacional disponível para leitura no Hub.
 * Livros são recursos de aprendizado que contêm conteúdo sobre gramática
 * inglesa, lore do jogo e outros tópicos educacionais.
 * </p>
 * <p>
 * Organização dos livros:
 * - Por tipo: ficção, técnico, gramática, histórico
 * - Por dificuldade: easy, medium, hard
 * - Conteúdo completo disponível para leitura
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    /**
     * Identificador único do livro.
     */
    private Integer id;

    /**
     * Título do livro.
     */
    private String title;

    /**
     * Conteúdo completo do livro (texto educacional).
     */
    private String content;

    /**
     * Tipo/categoria do livro (gramática, vocabulário, etc.).
     */
    private String type;

    /**
     * Nível de dificuldade de leitura (Facil, Medio, Dificil).
     */
    private String difficulty;
}
