package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma palavra-chave destacada em um diálogo educacional.
 * <p>
 * Palavras-chave são termos importantes nos diálogos multilíngues que
 * podem ser destacados e mostrar tradução para auxiliar no aprendizado de idiomas.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "dialog_keyword")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DialogKeyword {

    /** Identificador único da palavra-chave */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Diálogo ao qual esta palavra-chave pertence.
     * Relacionamento ManyToOne - um diálogo pode ter várias palavras-chave.
     */
    @ManyToOne
    @JoinColumn(name = "dialog_id", nullable = false)
    private Dialog dialog;

    /** Palavra em um dos idiomas (português ou inglês) */
    @Column(nullable = false)
    private String languageWord;

    /** Tradução da palavra para o outro idioma */
    @Column(nullable = false)
    private String translate;

    /** Se a palavra deve ser destacada visualmente no texto */
    @Column(nullable = false)
    private Boolean highlight = true;
}
