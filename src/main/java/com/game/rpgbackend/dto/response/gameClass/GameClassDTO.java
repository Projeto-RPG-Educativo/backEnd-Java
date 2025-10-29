package com.game.rpgbackend.dto.response.gameClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para informações de classe de personagem.
 * <p>
 * Retorna dados da classe sem expor relacionamentos (como lista de personagens).
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameClassDTO {
    /** ID único da classe */
    private Integer id;

    /** Nome da classe */
    private String name;

    /** Pontos de vida base */
    private Integer hp;

    /** Stamina base */
    private Integer stamina;

    /** Força base */
    private Integer strength;

    /** Inteligência base */
    private Integer intelligence;
}

