package com.game.rpgbackend.domain;

import com.game.rpgbackend.enums.AchievementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma conquista (achievement) de um personagem no jogo.
 * <p>
 * Conquistas são objetivos especiais que os jogadores podem alcançar durante
 * o jogo, registrando marcos importantes na progressão como vitórias em batalhas,
 * dano causado, questões respondidas, quests completadas, níveis alcançados, etc.
 * </p>
 * <p>
 * Cada conquista possui um progresso acumulado e um valor alvo definido pelo
 * {@link AchievementType}. Quando o progresso atinge ou supera o valor alvo,
 * a conquista é marcada como completada e a data de desbloqueio é registrada.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 2.0
 * @since 1.0
 */
@Entity
@Table(name = "achievements",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"character_id", "type"},
                name = "uk_achievement_character_type"
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {

    /**
     * Identificador único da conquista.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Personagem que está progredindo ou desbloqueou esta conquista.
     * <p>
     * Relacionamento ManyToOne - um personagem pode ter múltiplas conquistas,
     * mas cada registro de conquista pertence a apenas um personagem.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    /**
     * Tipo da conquista que define o nome, descrição e valor alvo.
     * <p>
     * O tipo determina os critérios necessários para completar a conquista
     * (ex: WIN_10_BATTLES, DEAL_1000_DAMAGE, COMPLETE_10_QUESTS).
     * </p>
     *
     * @see AchievementType
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AchievementType type;

    /**
     * Progresso atual da conquista.
     * <p>
     * Representa quantas unidades do objetivo foram alcançadas
     * (ex: quantas batalhas vencidas, quanto dano causado, etc.).
     * </p>
     */
    @Column(name = "progress", nullable = false)
    private int progress = 0;

    /**
     * Indica se a conquista foi completada.
     * <p>
     * Uma conquista é considerada completa quando o progresso
     * atinge ou supera o {@code targetValue} definido no {@link AchievementType}.
     * </p>
     */
    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted = false;

    /**
     * Data e hora em que a conquista foi desbloqueada.
     * <p>
     * Este campo permanece {@code null} enquanto a conquista não for completada.
     * É preenchido automaticamente quando {@link #isCompleted} se torna {@code true}.
     * </p>
     */
    @Column(name = "unlocked_at")
    private LocalDateTime unlockedAt;

    /**
     * Verifica se o progresso atual é suficiente para completar a conquista.
     * <p>
     * Se o progresso atingir ou superar o valor alvo e a conquista ainda não
     * estiver marcada como completa, este método:
     * <ul>
     *   <li>Marca {@link #isCompleted} como {@code true}</li>
     *   <li>Define {@link #unlockedAt} com a data/hora atual</li>
     *   <li>Retorna {@code true} indicando que foi completada agora</li>
     * </ul>
     * </p>
     *
     * @return {@code true} se a conquista foi completada nesta chamada,
     *         {@code false} caso contrário (já estava completa ou progresso insuficiente)
     */
    public boolean checkCompletion() {
        if (!isCompleted && progress >= type.getTargetValue()) {
            isCompleted = true;
            unlockedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    /**
     * Incrementa o progresso da conquista com o valor especificado.
     * <p>
     * Após incrementar, verifica automaticamente se a conquista foi completada.
     * </p>
     *
     * @param amount quantidade a ser adicionada ao progresso atual
     * @return {@code true} se a conquista foi completada com este incremento,
     *         {@code false} caso contrário
     */
    public boolean addProgress(int amount) {
        if (!isCompleted) {
            this.progress += amount;
            return checkCompletion();
        }
        return false;
    }

    /**
     * Retorna o percentual de progresso da conquista.
     *
     * @return valor entre 0 e 100 representando o percentual de conclusão
     */
    public double getProgressPercentage() {
        if (type.getTargetValue() == 0) {
            return 0.0;
        }
        return Math.min(100.0, (progress * 100.0) / type.getTargetValue());
    }
}
