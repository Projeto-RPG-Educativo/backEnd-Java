package com.game.rpgbackend.dto.request.battle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de requisição para submeter resposta a uma questão durante a batalha.
 * <p>
 * As batalhas no sistema RPG incluem questões que devem ser respondidas
 * corretamente para obter vantagens ou evitar danos. Este DTO contém
 * a resposta do jogador para uma questão específica.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
public class SubmitAnswerRequest {

    /**
     * ID único da batalha em andamento.
     */
    @NotNull(message = "O ID da batalha é obrigatório")
    private Long battleId;

    /**
     * ID da questão sendo respondida.
     */
    @NotNull(message = "O ID da questão é obrigatório")
    private Integer questionId;

    /**
     * Resposta fornecida pelo jogador para a questão.
     */
    @NotBlank(message = "A resposta é obrigatória")
    private String answer;
}
