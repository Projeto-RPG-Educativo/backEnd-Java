package com.game.rpgbackend.dto.request.hub;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requisição para aceitar uma quest.
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptQuestRequest {

    @NotNull(message = "ID da quest é obrigatório")
    private Integer questId;

    /**
     * ID do personagem que está aceitando a quest.
     * Obrigatório para suportar múltiplos personagens por usuário.
     */
    @NotNull(message = "ID do personagem é obrigatório")
    private Integer characterId;
}

