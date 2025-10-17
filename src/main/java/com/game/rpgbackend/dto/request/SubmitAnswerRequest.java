package com.game.rpgbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de requisição para submeter uma resposta a uma questão durante a batalha.
 */
@Data
public class SubmitAnswerRequest {

    @NotNull(message = "O ID da batalha é obrigatório")
    private Long battleId;

    @NotNull(message = "O ID da questão é obrigatório")
    private Integer questionId;

    @NotBlank(message = "A resposta é obrigatória")
    private String answer;
}

