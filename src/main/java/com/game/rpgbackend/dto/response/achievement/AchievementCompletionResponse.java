package com.game.rpgbackend.dto.response.achievement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta contendo informações sobre o percentual de conclusão de conquistas.
 * <p>
 * Retorna estatísticas completas sobre o progresso de conquistas de um personagem,
 * incluindo o percentual de conclusão, número de conquistas completadas e total de conquistas.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementCompletionResponse {

    /**
     * Percentual de conquistas completadas (0-100).
     */
    private double percentage;

    /**
     * Número de conquistas completadas pelo personagem.
     */
    private long completedCount;

    /**
     * Número total de conquistas disponíveis no jogo.
     */
    private long totalCount;
}

