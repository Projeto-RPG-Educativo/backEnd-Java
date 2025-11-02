package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa uma classe de personagem no sistema RPG.
 * <p>
 * Define os atributos base para cada tipo de classe disponível no jogo,
 * como Guerreiro, Mago, Arqueiro, Paladino, Tank, Bardo e Ladino.
 * Cada classe possui atributos iniciais únicos que influenciam o gameplay.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@Table(name = "class")
@NoArgsConstructor
@AllArgsConstructor
public class GameClass {

    /** Identificador único da classe */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nome da classe (ex: Guerreiro, Mago, Arqueiro, Paladino, Tank, Bardo, Ladino).
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * Pontos de vida base que personagens desta classe começam.
     */
    @Column(nullable = false)
    private Integer hp;

    /**
     * Stamina base da classe (energia/resistência).
     */
    @Column
    private Integer stamina;

    /**
     * Força base da classe (influencia dano físico).
     */
    @Column
    private Integer strength;

    /**
     * Inteligência base da classe (influencia dano mágico e resolução de questões).
     */
    @Column
    private Integer intelligence;

    /**
     * Defesa base da classe (reduz dano recebido).
     */
    @Column
    private Integer defense;

    /**
     * Agilidade base da classe (influencia velocidade e evasão).
     */
    @Column
    private Integer agility;

    /**
     * Carisma base da classe (influencia medo, charme e manipulação).
     */
    @Column
    private Integer charisma;

    /**
     * Sorte base da classe (influencia percepção, acaso e favorecido pela deusa do RNG).
     */
    @Column
    private Integer luck;

    /**
     * Lista de personagens que possuem esta classe.
     * Relacionamento OneToMany - uma classe pode ter vários personagens.
     */
    @OneToMany(mappedBy = "gameClass", cascade = CascadeType.ALL)
    private List<Character> characters;
}
