package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um monstro/inimigo no sistema de batalhas.
 * <p>
 * Monstros são os adversários que os personagens enfrentam em batalhas.
 * Cada monstro possui atributos como HP e dano que determinam sua força.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@Table(name = "monster")
@NoArgsConstructor
@AllArgsConstructor
public class Monster {

    /** Identificador único do monstro */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nome do monstro */
    @Column(nullable = false)
    private String monsterName;

    /** Pontos de vida do monstro */
    @Column(nullable = false)
    private Integer hp;

    /** Quantidade de dano que o monstro causa por ataque */
    @Column(nullable = false)
    private Integer monsterDamage;

    /** Defesa do monstro (reduz dano recebido) */
    @Column(nullable = false)
    private Integer defense;
}
