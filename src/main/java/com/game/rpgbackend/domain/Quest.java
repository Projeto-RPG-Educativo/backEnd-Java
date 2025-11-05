package com.game.rpgbackend.domain;

import com.game.rpgbackend.enums.QuestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa uma missão/quest no jogo.
 * <p>
 * Quests são missões que personagens podem completar para ganhar
 * recompensas como XP, ouro e itens. Podem envolver múltiplos personagens.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "quest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quest {

    /** Identificador único da quest */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Título único da quest */
    @Column(nullable = false, unique = true)
    private String title;

    /** Descrição detalhada da quest e seus objetivos */
    @Column(nullable = false)
    private String description;

    /** Quantidade de XP ganha ao completar a quest */
    @Column(name = "xp_ganho")
    private Integer xpReward;

    /** Quantidade de ouro ganha ao completar a quest */
    @Column(name = "ouro_ganho")
    private Integer goldReward;

    /** Tipo da quest (ANSWER_QUESTIONS, DEFEAT_MONSTER, WIN_BATTLES, REACH_LEVEL) */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestType type;

    /** Valor alvo necessário para completar a quest (ex: 15 perguntas, ID do monstro) */
    @Column(name = "target_value")
    private Integer targetValue;

    /** ID específico do alvo (ex: ID do monstro específico para DEFEAT_MONSTER) */
    @Column(name = "target_id")
    private Integer targetId;

    /**
     * Personagens que estão realizando ou completaram esta quest.
     * Relacionamento OneToMany através de tabela de junção CharacterQuest.
     */
    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL)
    private List<CharacterQuest> characters;

    /**
     * Itens dados como recompensa ao completar a quest.
     */
    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL)
    private List<QuestRewardItem> reward;
}
