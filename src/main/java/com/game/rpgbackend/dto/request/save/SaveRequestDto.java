package com.game.rpgbackend.dto.request.save;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * DTO de requisição para salvar um jogo. `currentState` é recebido como JSON.
 */

@Data
public class SaveRequestDto {
    private Long userId;
    private Long characterId;
    private String slotName;

    @NotNull(message = "currentState é obrigatório")
    private JsonNode currentState;
}