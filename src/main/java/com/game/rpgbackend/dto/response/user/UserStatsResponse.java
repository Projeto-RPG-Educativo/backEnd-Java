package com.game.rpgbackend.dto.response.user;

import lombok.Data;

/**
 * DTO de resposta para estatísticas do usuário.
 * <p>
 * Contém todas as estatísticas de jogo do usuário,
 * incluindo batalhas, questões e taxa de acerto.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
public class UserStatsResponse {
    /** Nível atual do usuário */
    private Integer level;

    /** Total de batalhas vencidas */
    private Integer totalBatalhasVencidas;

    /** Total de batalhas perdidas */
    private Integer totalBatalhasPerdidas;

    /** Total de questões respondidas corretamente */
    private Integer totalQuestoesCorretas;

    /** Total de questões respondidas incorretamente */
    private Integer totalQuestoesErradas;

    /** Taxa de acerto em questões (0.0 a 100.0) */
    private Double taxaAcerto;

    /** Experiência total acumulada */
    private Integer xpTotal;
}
