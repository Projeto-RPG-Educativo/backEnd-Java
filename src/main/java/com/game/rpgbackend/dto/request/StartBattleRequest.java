package com.game.rpgbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de requisição para iniciar uma batalha.
 */
@Data
public class StartBattleRequest {

    @NotNull(message = "O ID do monstro é obrigatório")
    private Integer monsterId;

    @NotBlank(message = "A dificuldade é obrigatória")
    private String difficulty;
}

