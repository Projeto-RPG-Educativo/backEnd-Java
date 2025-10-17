package com.game.rpgbackend.dto.response;

import lombok.Data;

@Data
public class UserStatsResponse {
    private Integer level;
    private Integer totalBatalhasVencidas;
    private Integer totalBatalhasPerdidas;
    private Integer totalQuestoesCorretas;
    private Integer totalQuestoesErradas;
    private Double taxaAcerto;
    private Integer xpTotal;
}
