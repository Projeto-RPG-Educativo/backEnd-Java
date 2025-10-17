package com.game.rpgbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO de requisição para ação de batalha (attack, defend, useSkill).
 */
@Data
public class BattleActionRequest {

    @NotBlank(message = "A ação é obrigatória")
    private String action; // "attack", "defend", "useSkill"
}

