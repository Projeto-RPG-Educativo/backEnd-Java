package com.game.rpgbackend.dto.response.battle;

import com.game.rpgbackend.enums.QuestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para informações de progresso de quest dentro da resposta de batalha.
 * <p>
 * Enviado junto com o resultado da batalha para que o frontend
 * possa mostrar notificações de progresso em tempo real.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestProgressDto {

    /** ID da quest */
    private Integer questId;

    /** Título da quest */
    private String questTitle;

    /** Tipo da quest */
    private QuestType questType;

    /** Progresso atual */
    private Integer currentProgress;

    /** Valor alvo */
    private Integer targetValue;

    /** Se a quest foi completada nesta ação */
    private Boolean justCompleted;

    /** Mensagem de progresso (ex: "5/15 perguntas acertadas") */
    private String progressMessage;
}

