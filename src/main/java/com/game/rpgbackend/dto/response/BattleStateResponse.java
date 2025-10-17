package com.game.rpgbackend.dto.response;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * DTO de resposta para o estado de batalha.
 */
@Data
public class BattleStateResponse {

    private Long battleId;
    private String difficulty;
    private CharacterBattleInfo character;
    private MonsterBattleInfo monster;
    private QuestionInfo currentQuestion;
    private Boolean isFinished = false;
    private Boolean bardChallengeActive = false;
    private String turnResult;

    @Data
    public static class CharacterBattleInfo {
        private Integer id;
        private Integer hp;
        private Integer energy;
        private String className;
        private Integer strength;
        private Integer intelligence;
        private Integer level;
        private Integer xp;
        private Boolean isDefending = false;
        private Map<String, Object> effects;
    }

    @Data
    public static class MonsterBattleInfo {
        private Integer id;
        private Integer hp;
        private Integer dano;
        private String nome;
    }

    @Data
    public static class QuestionInfo {
        private Integer id;
        private String texto;
        private List<String> opcoes;
        private Integer nivelMinimo;
        private String difficulty;
    }
}

