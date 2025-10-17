package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa um personagem do jogador no sistema.
 * Baseado no model Character do schema.prisma
 */
@Entity
@Table(name = "character")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer xp = 0; // XP atual para o próximo nível

    @Column(nullable = false)
    private Integer gold = 0; // Carteira atual do personagem

    @Column(nullable = false)
    private Integer hp; // Pontos de vida

    @Column(name = "energy")
    private Integer energy; // Energia atual

    @Column(name = "max_energy")
    private Integer maxEnergy; // Energia máxima

    @Column(name = "last_saved_at", nullable = false)
    private LocalDateTime lastSavedAt = LocalDateTime.now();

    // Relação: Um personagem pertence a um usuário
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relação: Um personagem tem uma classe
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private GameClass gameClass;

    // Relação com o inventário
    @OneToOne(mappedBy = "character", cascade = CascadeType.ALL)
    private Inventory inventory;
}
