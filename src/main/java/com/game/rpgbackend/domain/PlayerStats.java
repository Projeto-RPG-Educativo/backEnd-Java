package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa as estatísticas de um jogador.
 * <p>
 * Armazena todas as estatísticas e progressão do jogador, incluindo
 * nível, experiência total, batalhas, questões respondidas e habilidades desbloqueadas.
 * Cada usuário possui um único registro de estatísticas.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "player_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStats {

    /** Identificador único das estatísticas */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Usuário proprietário destas estatísticas.
     * Relacionamento OneToOne - cada usuário tem um único registro de stats.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /** Nível atual do jogador */
    @Column(nullable = false)
    private Integer level = 1;

    /** Total de pontos de experiência ganhos */
    @Column(name = "total_xp_ganhos", nullable = false)
    private Integer totalXpEarned = 0;

    /** Total de ouro ganho ao longo do jogo */
    @Column(name = "total_ouro_ganho", nullable = false)
    private Integer totalGoldEarned = 0;

    /** Total de batalhas vencidas */
    @Column(name = "battles_won", nullable = false)
    private Integer battlesWon = 0;

    /** Total de batalhas perdidas */
    @Column(name = "battles_lost", nullable = false)
    private Integer battlesLost = 0;

    /** Total de questões respondidas corretamente */
    @Column(name = "questions_right", nullable = false)
    private Integer questionsRight = 0;

    /** Total de questões respondidas incorretamente */
    @Column(name = "questions_wrong", nullable = false)
    private Integer questionsWrong = 0;

    /** Pontos de habilidade disponíveis para gastar */
    @Column(name = "skill_points", nullable = false)
    private Integer skillPoints = 0;

    /**
     * Habilidades desbloqueadas pelo jogador.
     * Relacionamento ManyToMany - um jogador pode ter várias skills e uma skill pode ser desbloqueada por vários jogadores.
     */
    @ManyToMany
    @JoinTable(
            name = "player_stats_skills",
            joinColumns = @JoinColumn(name = "player_stats_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> unlockedSkills;
}
