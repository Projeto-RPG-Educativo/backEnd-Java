package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "quest_reward_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(QuestRewardItemId.class)
public class QuestRewardItem {

    @Id
    @Column(name = "quest_id")
    private Integer questId;

    @Id
    @Column(name = "item_id")
    private Integer itemId;

    @ManyToOne
    @JoinColumn(name = "quest_id", insertable = false, updatable = false)
    private Quest quest;

    @ManyToOne
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private Item item;

    @Column(nullable = false)
    private Integer quantity = 1;
}
