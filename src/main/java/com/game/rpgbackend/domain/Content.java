package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa um conteúdo educacional no sistema.
 * <p>
 * Conteúdos são categorias temáticas que agrupam questões e diálogos
 * relacionados a um tópico específico de aprendizagem. Possuem um
 * nível mínimo para acesso.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@Table(name = "content")
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    /** Identificador único do conteúdo */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nome único do conteúdo educacional */
    @Column(nullable = false, unique = true)
    private String contentName;

    /** Descrição detalhada do conteúdo e temas abordados */
    @Column(nullable = false)
    private String description;

    /** Nível mínimo necessário para acessar este conteúdo */
    @Column(name = "level_minimo", nullable = false)
    private Integer minLevel = 1;

    /**
     * Questões relacionadas a este conteúdo.
     * Relacionamento OneToMany - um conteúdo pode ter várias questões.
     */
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<Question> questions;

    /**
     * Diálogos relacionados a este conteúdo.
     * Relacionamento OneToMany - um conteúdo pode ter vários diálogos.
     */
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<Dialog> dialogs;
}
