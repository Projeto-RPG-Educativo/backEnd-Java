package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma questão educacional no sistema de batalhas.
 * <p>
 * Durante as batalhas, jogadores devem responder questões corretamente
 * para obter vantagens. As questões possuem múltiplas opções e variam
 * em dificuldade e conteúdo educacional.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@Table(name = "question")
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    /** Identificador único da questão */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Texto da pergunta a ser apresentada ao jogador */
    @Column(name = "texto_pergunta", nullable = false)
    private String questionText;

    /** Primeira opção de resposta */
    @Column(name = "opcao_a", nullable = false)
    private String optionA;

    /** Segunda opção de resposta */
    @Column(name = "opcao_b", nullable = false)
    private String optionB;

    /** Terceira opção de resposta */
    @Column(name = "opcao_c", nullable = false)
    private String optionC;

    /** Letra da resposta correta (A, B ou C) */
    @Column(name = "resposta_correta", nullable = false)
    private String correctAnswer;

    /** Nível de dificuldade da questão (easy, medium, hard) */
    @Column(nullable = false)
    private String difficulty;

    /** Categoria do conteúdo da questão */
    @Column(nullable = false)
    private String questionContent;

    /** Nível mínimo do personagem para acessar esta questão */
    @Column(name = "level_minimo", nullable = false)
    private Integer minLevel = 1;

    /** Dica para auxiliar na resposta (habilidade especial da classe Ladino) */
    @Column (name="hint", nullable = false)
    private String hint;

    /**
     * Conteúdo educacional associado a esta questão.
     * Relacionamento ManyToOne - várias questões podem ter o mesmo conteúdo.
     */
    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;
}
