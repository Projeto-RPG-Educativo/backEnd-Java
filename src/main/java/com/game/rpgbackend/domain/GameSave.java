package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_save", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "slot_name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "slot_name", nullable = false)
    private String slotName;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt = LocalDateTime.now();

    @Column(name = "character_state", columnDefinition = "jsonb", nullable = false)
    private String characterState;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;
}
