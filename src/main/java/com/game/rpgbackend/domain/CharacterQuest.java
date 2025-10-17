package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "character_quest")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CharacterQuestId.class)
public class CharacterQuest {

    @Id
    @Column(name = "character_id")
    private Integer characterId;

    @Id
    @Column(name = "quest_id")
    private Integer questId;

    @ManyToOne
    @JoinColumn(name = "character_id", insertable = false, updatable = false)
    private Character character;

    @ManyToOne
    @JoinColumn(name = "quest_id", insertable = false, updatable = false)
    private Quest quest;

    @Column(nullable = false)
    private String status = "in_progress";
}
