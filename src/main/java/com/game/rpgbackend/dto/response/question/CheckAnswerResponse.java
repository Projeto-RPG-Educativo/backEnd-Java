package com.game.rpgbackend.dto.response.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para validação de respostas de questões.
 * <p>
 * Retorna o resultado da validação de uma resposta fornecida pelo jogador,
 * indicando se está correta ou incorreta. Usado no sistema de questões
 * fora das batalhas (modo treino ou estudo).
 * </p>
 * <p>
 * Resposta simples e direta:
 * - true: Resposta correta
 * - false: Resposta incorreta
 * </p>
 * <p>
 * Em batalhas, a validação de respostas é processada através do
 * BattleService e retorna BattleStateResponse com mais detalhes.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckAnswerResponse {

    /**
     * Indica se a resposta fornecida está correta.
     * true = correta, false = incorreta.
     */
    private Boolean correct;
}
