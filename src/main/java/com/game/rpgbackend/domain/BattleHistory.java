package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "battle_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String result;

    @Column(name = "enemy_name", nullable = false)
    private String enemyName;

    @Column(nullable = false)
    private Integer experience;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();
}
