package com.game.rpgbackend.dto.response.hub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para NPCs (Non-Player Characters) do Palco da Retórica.
 * <p>
 * Representa um personagem não-jogável com o qual o jogador pode interagir
 * no Hub. NPCs podem oferecer diálogos, quests, serviços (vendas, treinamento)
 * e informações sobre o lore do jogo.
 * </p>
 * <p>
 * Tipos de NPC:
 * - Mercador: Vende itens e equipamentos
 * - Mentor: Oferece conselhos e ensina mecânicas
 * - Guarda: Protege áreas específicas
 * - QuestGiver: Oferece missões e quests
 * </p>
 * <p>
 * Usado no frontend para:
 * - Exibir NPCs disponíveis por localização
 * - Mostrar informações ao clicar no NPC
 * - Iniciar diálogos e interações
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NpcDto {

    /**
     * Identificador único do NPC.
     */
    private Integer id;

    /**
     * Nome do NPC.
     */
    private String name;

    /**
     * Descrição e história do NPC.
     */
    private String description;

    /**
     * Tipo/função do NPC (mercador, mentor, guarda, questgiver, etc.).
     */
    private String type;

    /**
     * Localização onde o NPC pode ser encontrado no jogo.
     */
    private String location;
}
