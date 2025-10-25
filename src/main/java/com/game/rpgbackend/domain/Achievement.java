package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma conquista desbloqueada por um usuário.
 * <p>
 * Conquistas são objetivos especiais que jogadores podem alcançar
 * durante o jogo, registrando marcos importantes na progressão.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "achievement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {

    /** Identificador único da conquista */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Usuário que desbloqueou esta conquista.
     * Relacionamento ManyToOne - um usuário pode ter várias conquistas.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Nome da conquista */
    @Column(nullable = false)
    private String name;

    /** Descrição dos critérios e significado da conquista */
    @Column(nullable = false)
    private String description;

    /** Data e hora em que a conquista foi desbloqueada */
    @Column(name = "unlocked_at", nullable = false)
    private LocalDateTime unlockedAt = LocalDateTime.now();
}
