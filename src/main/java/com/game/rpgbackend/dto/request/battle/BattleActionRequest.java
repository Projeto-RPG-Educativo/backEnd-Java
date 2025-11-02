package com.game.rpgbackend.dto.request.battle;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO de requisição para ações de batalha.
 * <p>
 * Define a ação que um personagem pode realizar durante uma batalha,
 * como atacar, defender ou usar uma habilidade especial.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
public class BattleActionRequest {

    /**
     * Tipo de ação a ser executada na batalha.
     * Valores possíveis: "attack", "defend", "useSkill"
     */
    @NotBlank(message = "A ação é obrigatória")
    private String action; // "attack", "defend", "useSkill"
}
