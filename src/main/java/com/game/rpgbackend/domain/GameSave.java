package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidade que representa um salvamento de jogo.
 * <p>
 * Permite aos jogadores salvar o progresso de seus personagens em slots específicos.
 * O estado do personagem é armazenado em formato JSONB para flexibilidade.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "game_save", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "slot_name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSave {

    /** Identificador único do salvamento */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nome do slot de salvamento (ex: "Save 1", "Save 2") */
    @Column(name = "slot_name", nullable = false)
    private String slotName;

    /** Data e hora em que o jogo foi salvo */
    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt = LocalDateTime.now();

    /** Estado completo do personagem em formato JSON */
    @Column(name = "character_state", columnDefinition = "jsonb", nullable = false)
    private String characterState;

    /**
     * Usuário proprietário deste salvamento.
     * Relacionamento ManyToOne - um usuário pode ter vários salvamentos.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Personagem salvo neste slot.
     * Relacionamento ManyToOne - um personagem pode ter vários salvamentos.
     */
    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;
}
