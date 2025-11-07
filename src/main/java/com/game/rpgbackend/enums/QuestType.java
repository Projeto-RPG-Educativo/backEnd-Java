package com.game.rpgbackend.enums;

/**
 * Enumeração dos tipos de quests disponíveis no jogo.
 * <p>
 * Define os diferentes objetivos que uma quest pode ter:
 * - ANSWER_QUESTIONS: Acertar um número específico de perguntas
 * - DEFEAT_MONSTER: Derrotar um monstro específico
 * - WIN_BATTLES: Vencer um número de batalhas
 * - REACH_LEVEL: Alcançar um nível específico
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
public enum QuestType {
    /**
     * Quest para acertar um número específico de perguntas.
     * Target = número de perguntas a acertar
     */
    ANSWER_QUESTIONS,

    /**
     * Quest para derrotar um monstro específico.
     * Target = ID do monstro a derrotar
     */
    DEFEAT_MONSTER,

    /**
     * Quest para vencer um número de batalhas.
     * Target = número de batalhas a vencer
     */
    WIN_BATTLES,

    /**
     * Quest para alcançar um nível específico.
     * Target = nível a alcançar
     */
    REACH_LEVEL,

    /**
     * Quest para causar uma quantidade específica de dano.
     * Target = quantidade de dano ao causar
     */
    DEAL_DAMAGE
}

