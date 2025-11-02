package com.game.rpgbackend.dto.response.save;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de resposta para salvamentos de jogo.
 * <p>
 * Representa um save completo de um personagem em um slot específico.
 * Permite ao jogador visualizar seus salvamentos e carregar o progresso
 * de jogos anteriores.
 * </p>
 * <p>
 * Informações do save:
 * - Slot onde foi salvo (slot1, slot2, quicksave, etc.)
 * - Data e hora do salvamento
 * - Estado completo do personagem em JSON
 * - Referências ao usuário e personagem
 * </p>
 * <p>
 * O campo characterState contém JSON com:
 * - HP, XP, ouro atuais
 * - Inventário completo
 * - Progresso em quests
 * - Localização atual
 * - Habilidades desbloqueadas
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSaveDto {

    /**
     * Identificador único do salvamento.
     */
    private Integer id;

    /**
     * Nome do slot de salvamento (ex: "slot1", "slot2", "quicksave").
     */
    private String slotName;

    /**
     * Data e hora em que o jogo foi salvo.
     */
    private LocalDateTime savedAt;

    /**
     * Estado completo do personagem em formato JSON.
     * Contém HP, XP, ouro, inventário, progresso em quests, etc.
     */
    private String characterState;

    /**
     * ID do usuário proprietário deste salvamento.
     */
    private Integer userId;

    /**
     * ID do personagem salvo.
     */
    private Integer characterId;
}
