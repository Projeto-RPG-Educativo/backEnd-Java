package com.game.rpgbackend.dto.response.hub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para diálogos de NPCs no Palco da Retórica.
 * <p>
 * Representa uma fala individual de um NPC com possível resposta do jogador.
 * Diferente dos Dialog educacionais multilíngues, estes são diálogos
 * narrativos do jogo para interação com personagens não-jogáveis.
 * </p>
 * <p>
 * Uso no frontend:
 * - Exibir o conteúdo da fala do NPC
 * - Mostrar opção de resposta do jogador (se disponível)
 * - Permitir progressão de conversas e quests
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DialogueDto {

    /**
     * Identificador único do diálogo.
     */
    private Integer id;

    /**
     * Conteúdo da fala do NPC.
     */
    private String content;

    /**
     * Resposta opcional que o jogador pode dar (pode ser null).
     */
    private String response;
}
