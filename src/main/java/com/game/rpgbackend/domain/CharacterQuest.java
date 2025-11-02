package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Entidade que representa o progresso de um personagem em uma quest.
 * <p>
 * Esta tabela de junção gerencia quais quests cada personagem está
 * realizando e o status de conclusão de cada uma.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "character_quest")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CharacterQuestId.class)
public class CharacterQuest {

    /** ID do personagem (parte da chave composta) */
    @Id
    @Column(name = "character_id")
    private Integer characterId;

    /** ID da quest (parte da chave composta) */
    @Id
    @Column(name = "quest_id")
    private Integer questId;

    /**
     * Personagem realizando a quest.
     * Relacionamento ManyToOne.
     */
    @ManyToOne
    @JoinColumn(name = "character_id", insertable = false, updatable = false)
    private Character character;

    /**
     * Quest sendo realizada.
     * Relacionamento ManyToOne.
     */
    @ManyToOne
    @JoinColumn(name = "quest_id", insertable = false, updatable = false)
    private Quest quest;

    /** Status da quest (in_progress, completed, failed) */
    @Column(nullable = false)
    private String status = "in_progress";
}
