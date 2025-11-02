package com.game.rpgbackend.dto.response.tutorial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para NPCs do tutorial.
 * <p>
 * Representa um NPC mentor que guia o jogador durante o tutorial inicial.
 * NPCs de tutorial são personagens especiais que ensinam as mecânicas
 * básicas do jogo de forma narrativa e interativa.
 * </p>
 * <p>
 * Informações do NPC:
 * - Nome e descrição do personagem
 * - Tipo (mentor, guia, instrutor)
 * - Localização inicial no tutorial
 * </p>
 * <p>
 * Usado no frontend para:
 * - Exibir avatar/sprite do NPC
 * - Mostrar nome do NPC nos diálogos
 * - Contextualizar quem está falando
 * - Criar imersão narrativa no tutorial
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NpcResponseDto {

    /**
     * Identificador único do NPC.
     */
    private Long id;

    /**
     * Nome do NPC mentor.
     */
    private String name;

    /**
     * Descrição e história do NPC.
     */
    private String description;

    /**
     * Tipo do NPC (mentor, guia, instrutor).
     */
    private String type;

    /**
     * Localização do NPC no tutorial.
     */
    private String location;
}
