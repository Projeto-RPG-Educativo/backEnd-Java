package com.game.rpgbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Classe de identificador composto para a entidade CharacterQuest.
 * <p>
 * Representa a chave primária composta pela combinação de
 * ID do personagem e ID da quest, usada no relacionamento many-to-many
 * entre Character e Quest para rastrear o progresso de quests.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterQuestId implements Serializable {

    /** Identificador do personagem */
    private Integer characterId;

    /** Identificador da quest */
    private Integer questId;
}
