package com.game.rpgbackend.dto.response.hub;

import com.game.rpgbackend.enums.QuestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para Quest da Torre do Conhecimento.
 * <p>
 * Contém todas as informações sobre uma quest (missão), incluindo
 * objetivos, recompensas e progresso atual do jogador.
 * </p>
 * <p>
 * <b>Tipos de Quest disponíveis:</b>
 * <ul>
 *   <li><b>ANSWER_QUESTIONS:</b> Acertar X perguntas em batalhas</li>
 *   <li><b>DEFEAT_MONSTER:</b> Derrotar um monstro específico X vezes</li>
 *   <li><b>WIN_BATTLES:</b> Vencer X batalhas (qualquer monstro)</li>
 *   <li><b>REACH_LEVEL:</b> Alcançar um nível específico</li>
 * </ul>
 * </p>
 * <p>
 * <b>Status possíveis:</b>
 * <ul>
 *   <li><b>null:</b> Quest disponível, ainda não aceita</li>
 *   <li><b>"in_progress":</b> Quest ativa, em andamento</li>
 *   <li><b>"completed":</b> Quest finalizada com sucesso</li>
 *   <li><b>"failed":</b> Quest abandonada pelo jogador</li>
 * </ul>
 * </p>
 * <p>
 * <b>Exemplo de resposta JSON:</b>
 * <pre>
 * {
 *   "id": 1,
 *   "title": "Domínio do Conhecimento",
 *   "description": "Acerte 15 perguntas em batalhas...",
 *   "xpReward": 500,
 *   "goldReward": 100,
 *   "type": "ANSWER_QUESTIONS",
 *   "targetValue": 15,
 *   "targetId": null,
 *   "targetName": null,
 *   "progress": 5,
 *   "status": "in_progress"
 * }
 * </pre>
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestDto {

    /**
     * Identificador único da quest.
     * Usado para aceitar ou abandonar a quest.
     */
    private Integer id;

    /**
     * Título da quest.
     * Exemplo: "Domínio do Conhecimento", "A Ameaça Errônea"
     */
    private String title;

    /**
     * Descrição detalhada da quest explicando o objetivo.
     * Exemplo: "Demonstre seu conhecimento acertando 15 perguntas em batalhas."
     */
    private String description;

    /**
     * Quantidade de XP (experiência) recebida ao completar a quest.
     * Varia de 200 (quests iniciantes) até 800+ (quests avançadas).
     */
    private Integer xpReward;

    /**
     * Quantidade de ouro (gold) recebida ao completar a quest.
     * Varia de 50 (quests iniciantes) até 150+ (quests avançadas).
     */
    private Integer goldReward;

    /**
     * Tipo da quest que define o objetivo:
     * <ul>
     *   <li>ANSWER_QUESTIONS - Acertar perguntas</li>
     *   <li>DEFEAT_MONSTER - Derrotar monstro específico</li>
     *   <li>WIN_BATTLES - Vencer batalhas</li>
     *   <li>REACH_LEVEL - Alcançar nível</li>
     * </ul>
     */
    private QuestType type;

    /**
     * Valor alvo necessário para completar a quest.
     * <ul>
     *   <li>Para ANSWER_QUESTIONS: número de perguntas a acertar (ex: 15)</li>
     *   <li>Para DEFEAT_MONSTER: número de vezes para derrotar (ex: 3)</li>
     *   <li>Para WIN_BATTLES: número de vitórias necessárias (ex: 10)</li>
     *   <li>Para REACH_LEVEL: nível a alcançar (ex: 5)</li>
     * </ul>
     */
    private Integer targetValue;

    /**
     * ID específico do alvo (usado apenas para DEFEAT_MONSTER).
     * Contém o ID do monstro que deve ser derrotado.
     * Null para outros tipos de quest.
     */
    private Integer targetId;

    /**
     * Nome descritivo do alvo (usado apenas para DEFEAT_MONSTER).
     * Exemplo: "Diabrete Errôneo", "Harpia Indagada"
     * Null para outros tipos de quest.
     */
    private String targetName;

    /**
     * Progresso atual do jogador na quest.
     * <ul>
     *   <li>0 se a quest ainda não foi aceita</li>
     *   <li>Número atual de progresso se a quest está ativa</li>
     *   <li>Igual a targetValue se a quest foi completada</li>
     * </ul>
     * Exemplo: 5/15 perguntas acertadas = progress: 5
     */
    private Integer progress;

    /**
     * Status atual da quest para o jogador:
     * <ul>
     *   <li>null - Quest disponível para aceitar</li>
     *   <li>"in_progress" - Quest ativa, em andamento</li>
     *   <li>"completed" - Quest finalizada com sucesso</li>
     *   <li>"failed" - Quest abandonada</li>
     * </ul>
     */
    private String status;
}

