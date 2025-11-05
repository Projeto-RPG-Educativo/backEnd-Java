package com.game.rpgbackend.dto.response.hub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta ao aceitar uma quest.
 * <p>
 * Retorna informações simplificadas sobre a quest aceita,
 * evitando referências circulares no JSON.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptQuestResponse {

    /** ID do personagem que aceitou a quest */
    private Integer characterId;

    /** Nome do personagem */
    private String characterName;

    /** ID da quest aceita */
    private Integer questId;

    /** Título da quest */
    private String questTitle;

    /** Descrição da quest */
    private String questDescription;

    /** Status da quest (sempre "in_progress" ao aceitar) */
    private String status;

    /** Progresso inicial (sempre 0 ao aceitar) */
    private Integer progress;

    /** Valor alvo da quest */
    private Integer targetValue;

    /** Mensagem de sucesso */
    private String message;
}

