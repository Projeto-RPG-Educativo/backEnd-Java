package com.game.rpgbackend.enums;

import lombok.Getter;

/**
 * Enumeração que define os tipos de conquistas (achievements) disponíveis no jogo.
 * <p>
 * Cada tipo de conquista possui:
 * <ul>
 *   <li>Nome descritivo da conquista</li>
 *   <li>Descrição detalhada do objetivo</li>
 *   <li>Valor alvo necessário para completar</li>
 * </ul>
 * </p>
 * <p>
 * As conquistas são categorizadas em:
 * <ul>
 *   <li><b>Batalha:</b> Vitórias em combate</li>
 *   <li><b>Dano:</b> Dano total causado</li>
 *   <li><b>Questões:</b> Questões corretas respondidas</li>
 *   <li><b>Quests:</b> Missões completadas</li>
 *   <li><b>Level:</b> Níveis alcançados</li>
 *   <li><b>Monstros:</b> Tipos específicos de monstros derrotados</li>
 * </ul>
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum AchievementType {

    // Conquistas de Batalha
    /**
     * Conquista desbloqueada ao vencer a primeira batalha.
     */
    WIN_FIRST_BATTLE("Primeira Vitória", "Vença sua primeira batalha", 1),

    /**
     * Conquista desbloqueada ao vencer 10 batalhas.
     */
    WIN_10_BATTLES("Guerreiro Iniciante", "Vença 10 batalhas", 10),

    /**
     * Conquista desbloqueada ao vencer 50 batalhas.
     */
    WIN_50_BATTLES("Veterano de Guerra", "Vença 50 batalhas", 50),

    /**
     * Conquista desbloqueada ao vencer 100 batalhas.
     */
    WIN_100_BATTLES("Lenda Viva", "Vença 100 batalhas", 100),

    // Conquistas de Dano
    /**
     * Conquista desbloqueada ao causar 1000 de dano total.
     */
    DEAL_1000_DAMAGE("Destruidor", "Cause 1000 de dano total", 1000),

    /**
     * Conquista desbloqueada ao causar 5000 de dano total.
     */
    DEAL_5000_DAMAGE("Aniquilador", "Cause 5000 de dano total", 5000),

    /**
     * Conquista desbloqueada ao causar 10000 de dano total.
     */
    DEAL_10000_DAMAGE("Devastador", "Cause 10000 de dano total", 10000),

    // Conquistas de Questões
    /**
     * Conquista desbloqueada ao acertar 10 questões.
     */
    ANSWER_10_QUESTIONS("Estudioso", "Acerte 10 questões", 10),

    /**
     * Conquista desbloqueada ao acertar 50 questões.
     */
    ANSWER_50_QUESTIONS("Sábio", "Acerte 50 questões", 50),

    /**
     * Conquista desbloqueada ao acertar 100 questões.
     */
    ANSWER_100_QUESTIONS("Mestre do Conhecimento", "Acerte 100 questões", 100),

    // Conquistas de Quests
    /**
     * Conquista desbloqueada ao completar a primeira quest.
     */
    COMPLETE_FIRST_QUEST("Aventureiro", "Complete sua primeira quest", 1),

    /**
     * Conquista desbloqueada ao completar 10 quests.
     */
    COMPLETE_10_QUESTS("Caçador de Recompensas", "Complete 10 quests", 10),

    /**
     * Conquista desbloqueada ao completar 25 quests.
     */
    COMPLETE_25_QUESTS("Herói Renomado", "Complete 25 quests", 25),

    // Conquistas de Level
    /**
     * Conquista desbloqueada ao alcançar o nível 5.
     */
    REACH_LEVEL_5("Aprendiz Avançado", "Alcance o nível 5", 5),

    /**
     * Conquista desbloqueada ao alcançar o nível 10.
     */
    REACH_LEVEL_10("Aventureiro Experiente", "Alcance o nível 10", 10),

    /**
     * Conquista desbloqueada ao alcançar o nível 20.
     */
    REACH_LEVEL_20("Campeão", "Alcance o nível 20", 20),

    // Conquistas de Monstros
    /**
     * Conquista desbloqueada ao derrotar um Goblin.
     */
    DEFEAT_GOBLIN("Caçador de Goblins", "Derrote um Goblin", 1),

    /**
     * Conquista desbloqueada ao derrotar um Dragão.
     */
    DEFEAT_DRAGON("Matador de Dragões", "Derrote um Dragão", 1),

    /**
     * Conquista desbloqueada ao derrotar 10 monstros.
     */
    DEFEAT_10_MONSTERS("Exterminador", "Derrote 10 monstros", 10);

    /**
     * Nome da conquista exibido ao jogador.
     */
    private final String name;

    /**
     * Descrição detalhada do objetivo da conquista.
     */
    private final String description;

    /**
     * Valor alvo necessário para completar a conquista.
     */
    private final int targetValue;

    /**
     * Construtor do enum.
     *
     * @param name nome da conquista
     * @param description descrição do objetivo
     * @param targetValue valor alvo para completar
     */
    AchievementType(String name, String description, int targetValue) {
        this.name = name;
        this.description = description;
        this.targetValue = targetValue;
    }
}
