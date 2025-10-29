package com.game.rpgbackend.dto.request.battle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de requisição para iniciar uma nova batalha.
 * <p>
 * Contém as informações necessárias para começar uma batalha,
 * incluindo o monstro alvo e o nível de dificuldade desejado.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
public class StartBattleRequest {

    /**
     * ID do monstro a ser enfrentado na batalha.
     */
    @NotNull(message = "O ID do monstro é obrigatório")
    private Integer monsterId;

    /**
     * Nível de dificuldade da batalha.
     * Valores possíveis: "easy", "medium", "hard"
     */
    @NotBlank(message = "A dificuldade é obrigatória")
    private String difficulty;
}
