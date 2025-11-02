package com.game.rpgbackend.dto.response.tutorial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para diálogos do tutorial.
 * <p>
 * Representa um diálogo introdutório que guia novos jogadores através
 * dos primeiros passos do sistema RPG. Os diálogos de tutorial são
 * apresentados sequencialmente por NPCs específicos do tutorial.
 * </p>
 * <p>
 * Estrutura do diálogo de tutorial:
 * - Conteúdo da fala do NPC mentor
 * - Resposta opcional do jogador
 * - Informações do NPC que apresenta o tutorial
 * </p>
 * <p>
 * Usado no frontend para:
 * - Exibir diálogos de onboarding
 * - Ensinar mecânicas básicas do jogo
 * - Guiar jogador pela primeira experiência
 * - Criar conexão narrativa inicial
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DialogueResponseDto {

    /**
     * Identificador único do diálogo de tutorial.
     */
    private Long id;

    /**
     * Conteúdo da fala do NPC mentor.
     */
    private String content;

    /**
     * Resposta sugerida para o jogador (pode ser null).
     */
    private String response;

    /**
     * Informações do NPC que apresenta este diálogo de tutorial.
     */
    private NpcResponseDto npc;
}