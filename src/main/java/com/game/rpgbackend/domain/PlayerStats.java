package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "player_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private Integer level = 1;

    @Column(name = "total_xp_ganhos", nullable = false)
    private Integer totalXpGanhos = 0;

    @Column(name = "total_ouro_ganho", nullable = false)
    private Integer totalOuroGanho = 0;

    @Column(name = "battles_won", nullable = false)
    private Integer battlesWon = 0;

    @Column(name = "battles_lost", nullable = false)
    private Integer battlesLost = 0;

    @Column(name = "questions_right", nullable = false)
    private Integer questionsRight = 0;

    @Column(name = "questions_wrong", nullable = false)
    private Integer questionsWrong = 0;

    @Column(name = "skill_points", nullable = false)
    private Integer skillPoints = 0;

    @ManyToMany
    @JoinTable(
            name = "player_stats_skills",
            joinColumns = @JoinColumn(name = "player_stats_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> unlockedSkills;
}
