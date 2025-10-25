package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidade que representa o histórico de batalhas de um usuário.
 * <p>
 * Registra cada batalha realizada pelo jogador, incluindo resultado,
 * inimigo enfrentado, experiência ganha e data da batalha.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "battle_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleHistory {

    /** Identificador único do registro de batalha */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Usuário que participou da batalha.
     * Relacionamento ManyToOne - um usuário pode ter várias batalhas registradas.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Resultado da batalha (vitória, derrota, empate) */
    @Column(nullable = false)
    private String result;

    /** Nome do inimigo enfrentado */
    @Column(name = "enemy_name", nullable = false)
    private String enemyName;

    /** Quantidade de experiência ganha na batalha */
    @Column(nullable = false)
    private Integer experience;

    /** Data e hora em que a batalha ocorreu */
    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();
}
