package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa um personagem do jogador no sistema.
 * <p>
 * Cada personagem pertence a um usuário e possui uma classe específica,
 * atributos como HP, XP, ouro, energia e um inventário associado.
 * Os personagens são a principal forma de interação do jogador com o jogo.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "character")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Character {

    /** Identificador único do personagem */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nome do personagem escolhido pelo jogador */
    @Column(nullable = false)
    private String name;

    /** Pontos de experiência atuais para progressão de nível */
    @Column(nullable = false)
    private Integer xp = 0;

    /** Quantidade de ouro (moeda do jogo) na carteira do personagem */
    @Column(nullable = false)
    private Integer gold = 0;

    /** Pontos de vida atuais do personagem */
    @Column(nullable = false)
    private Integer hp;

    /** Energia atual disponível para ações */
    @Column(name = "energy")
    private Integer energy;

    /** Energia máxima que o personagem pode ter */
    @Column(name = "max_energy")
    private Integer maxEnergy;

    /** Data e hora do último salvamento do personagem */
    @Column(name = "last_saved_at", nullable = false)
    private LocalDateTime lastSavedAt = LocalDateTime.now();

    /**
     * Usuário proprietário deste personagem.
     * Relacionamento ManyToOne - vários personagens podem pertencer a um usuário.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Classe do personagem (Guerreiro, Mago, etc.).
     * Relacionamento ManyToOne - vários personagens podem ter a mesma classe.
     */
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private GameClass gameClass;

    /**
     * Inventário único do personagem.
     * Relacionamento OneToOne - cada personagem tem um inventário.
     */
    @OneToOne(mappedBy = "character", cascade = CascadeType.ALL)
    private Inventory inventory;
}
