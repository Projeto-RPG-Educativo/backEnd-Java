package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa uma classe de personagem (Guerreiro, Mago, Arqueiro, etc.).
 * Baseado no model Class do schema.prisma
 */
@Data
@Entity
@Table(name = "class")
@NoArgsConstructor
@AllArgsConstructor
public class GameClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nome da classe (ex: Guerreiro, Mago, Arqueiro).
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * Pontos de vida base da classe.
     */
    @Column(nullable = false)
    private Integer hp;

    /**
     * Stamina base da classe.
     */
    @Column
    private Integer stamina;

    /**
     * Força base da classe.
     */
    @Column
    private Integer strength;

    /**
     * Inteligência base da classe.
     */
    @Column
    private Integer intelligence;

    /**
     * Lista de personagens que possuem esta classe.
     */
    @OneToMany(mappedBy = "gameClass", cascade = CascadeType.ALL)
    private List<Character> characters;
}

