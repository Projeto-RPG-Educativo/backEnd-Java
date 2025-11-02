package com.game.rpgbackend.dto.response.save;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de resposta simplificado para listagem de salvamentos.
 * <p>
 * Versão resumida de GameSaveDto usada para exibir lista de saves
 * disponíveis sem carregar o estado completo (JSON). Ideal para
 * tela de seleção de saves onde só precisamos de informações básicas.
 * </p>
 * <p>
 * Informações essenciais:
 * - Slot e data do salvamento
 * - Identificação do personagem
 * - Nome e classe para exibição rápida
 * </p>
 * <p>
 * Usado no frontend para:
 * - Listar saves disponíveis do usuário
 * - Exibir preview de cada save (personagem, data)
 * - Permitir seleção para carregar jogo completo
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveResponseDto {

    /**
     * Identificador único do salvamento.
     */
    private Integer id;

    /**
     * Nome do slot de salvamento (ex: "slot1", "slot2", "auto-save").
     */
    private String slotName;

    /**
     * Data e hora em que o jogo foi salvo.
     */
    private LocalDateTime savedAt;

    /**
     * ID do personagem salvo.
     */
    private Integer characterId;

    /**
     * Nome do personagem para exibição.
     */
    private String characterName;

    /**
     * Classe do personagem (Lutador, Mago, etc.).
     */
    private String characterClass;
}
