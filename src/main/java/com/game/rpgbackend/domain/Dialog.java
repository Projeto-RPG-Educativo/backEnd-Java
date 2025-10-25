package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa um diálogo educacional multilíngue.
 * <p>
 * Diálogos são textos em português e inglês associados a conteúdos educacionais,
 * permitindo prática de idiomas. Podem conter palavras-chave para exploração.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "dialog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dialog {

    /** Identificador único do diálogo */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Conteúdo educacional ao qual este diálogo pertence.
     * Relacionamento ManyToOne - vários diálogos podem ter o mesmo conteúdo.
     */
    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    /** Texto do diálogo em português */
    @Column(nullable = false)
    private String ptDialogue;

    /** Texto do diálogo em inglês */
    @Column(nullable = false)
    private String enDialogue;

    /** Nível mínimo necessário para acessar este diálogo */
    @Column(name = "level_minimo", nullable = false)
    private Integer minLevel = 1;

    /**
     * Palavras-chave importantes presentes neste diálogo.
     * Relacionamento OneToMany - um diálogo pode ter várias palavras-chave.
     */
    @OneToMany(mappedBy = "dialog", cascade = CascadeType.ALL)
    private List<DialogKeyword> keywords;
}
