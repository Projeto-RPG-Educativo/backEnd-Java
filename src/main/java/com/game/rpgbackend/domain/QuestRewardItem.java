package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Entidade que representa itens dados como recompensa ao completar quests.
 * <p>
 * Esta tabela de junção define quais itens e em que quantidade
 * são recebidos ao completar uma quest específica.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "quest_reward_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(QuestRewardItemId.class)
public class QuestRewardItem {

    /** ID da quest (parte da chave composta) */
    @Id
    @Column(name = "quest_id")
    private Integer questId;

    /** ID do item (parte da chave composta) */
    @Id
    @Column(name = "item_id")
    private Integer itemId;

    /**
     * Quest que dá este item como recompensa.
     * Relacionamento ManyToOne.
     */
    @ManyToOne
    @JoinColumn(name = "quest_id", insertable = false, updatable = false)
    private Quest quest;

    /**
     * Item dado como recompensa.
     * Relacionamento ManyToOne.
     */
    @ManyToOne
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private Item item;

    /** Quantidade do item dada como recompensa */
    @Column(nullable = false)
    private Integer quantity = 1;
}
