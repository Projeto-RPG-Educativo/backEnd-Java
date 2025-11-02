package com.game.rpgbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Classe de identificador composto para a entidade QuestRewardItem.
 * <p>
 * Representa a chave primária composta pela combinação de
 * ID da quest e ID do item, usada no relacionamento many-to-many
 * entre Quest e Item para definir recompensas.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestRewardItemId implements Serializable {

    /** Identificador da quest */
    private Integer questId;

    /** Identificador do item recompensa */
    private Integer itemId;
}
