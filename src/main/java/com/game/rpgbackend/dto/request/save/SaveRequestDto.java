package com.game.rpgbackend.dto.request.save;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * DTO de requisição para criar ou atualizar um salvamento de jogo.
 * <p>
 * Permite salvar o estado completo de um personagem em um slot específico.
 * O estado do jogo é armazenado em formato JSON para máxima flexibilidade.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
public class SaveRequestDto {

    /** ID do usuário proprietário do salvamento */
    private Long userId;

    /** ID do personagem sendo salvo */
    private Long characterId;

    /** Nome do slot de salvamento (ex: "slot1", "slot2") */
    private String slotName;

    /**
     * Estado completo do jogo em formato JSON.
     * Inclui HP, XP, ouro, inventário e outros dados do personagem.
     */
    @NotNull(message = "currentState é obrigatório")
    private JsonNode currentState;
}