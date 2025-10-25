package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um diálogo específico de um NPC.
 * <p>
 * Diálogos (Dialogue) são falas individuais de NPCs que podem ter
 * respostas associadas, diferentes dos Dialog educacionais multilíngues.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "dialogue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dialogue {

    /** Identificador único do diálogo */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Conteúdo da fala do NPC */
    @Column(nullable = false)
    private String content;

    /** Resposta opcional que o jogador pode dar */
    @Column
    private String response;

    /**
     * NPC que fala este diálogo.
     * Relacionamento ManyToOne - um NPC pode ter vários diálogos.
     */
    @ManyToOne
    @JoinColumn(name = "npc_id", nullable = false)
    private NPC npc;
}
