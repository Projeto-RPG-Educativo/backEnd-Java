package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "question")
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "texto_pergunta", nullable = false)
    private String textoPergunta;

    @Column(name = "opcao_a", nullable = false)
    private String opcaoA;

    @Column(name = "opcao_b", nullable = false)
    private String opcaoB;

    @Column(name = "opcao_c", nullable = false)
    private String opcaoC;

    @Column(name = "resposta_correta", nullable = false)
    private String respostaCorreta;

    @Column(nullable = false)
    private String dificuldade;

    @Column(nullable = false)
    private String conteudo;

    @Column(name = "level_minimo", nullable = false)
    private Integer levelMinimo = 1;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;
}
