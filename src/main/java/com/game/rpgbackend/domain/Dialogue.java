package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dialogue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dialogue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String content;

    @Column
    private String response;

    @ManyToOne
    @JoinColumn(name = "npc_id", nullable = false)
    private NPC npc;
}
