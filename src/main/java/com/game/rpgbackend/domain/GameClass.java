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
 * @author D0UGH5
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
     * Lista de personagens que possuem esta classe.
     * Relacionamento OneToMany - uma classe pode ter vários personagens.
     */
    @OneToMany(mappedBy = "gameClass", cascade = CascadeType.ALL)
    private List<Character> characters;
}
