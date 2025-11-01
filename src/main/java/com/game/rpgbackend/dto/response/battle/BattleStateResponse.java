package com.game.rpgbackend.dto.response.battle;

import com.game.rpgbackend.dto.battle.BattleEffect;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * DTO de resposta para o estado completo de uma batalha.
 * <p>
 * Contém todas as informações sobre o estado atual de uma batalha em andamento,
 * incluindo dados do personagem, do monstro e da questão atual.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
public class BattleStateResponse {

    /** ID único da batalha */
    private Long battleId;

    /** Nível de dificuldade da batalha */
    private String difficulty;

    /** Informações do personagem na batalha */
    private CharacterBattleInfo character;

    /** Informações do monstro na batalha */
    private MonsterBattleInfo monster;

    /** Questão atual que deve ser respondida */
    private QuestionInfo currentQuestion;

    /** Indica se a batalha foi finalizada */
    private Boolean isFinished = false;

    /** Indica se o desafio especial do Bardo está ativo */
    private Boolean bardChallengeActive = false;

    /** Resultado do último turno executado */
    private String turnResult;

    /** Dano causado pelo personagem no último turno */
    private Integer characterDamageDealt;

    /** Dano causado pelo monstro no último turno */
    private Integer monsterDamageDealt;

    /** Ação tomada pelo monstro no último turno (ex: "attack", "defend", "skill") */
    private String monsterAction;

    /** Indica se está aguardando o turno do monstro ser processado */
    private Boolean waitingForMonsterTurn = false;

    /** Dano pendente do personagem que será aplicado ao monstro após sua ação */
    private Integer pendingDamageToMonster = 0;

    /** Indica se é o turno do jogador (true) ou do monstro (false) */
    private Boolean isPlayerTurn = true;

    /** Efeitos ativos no personagem durante a batalha */
    private List<BattleEffect> characterActiveEffects;

    /** Efeitos ativos no monstro durante a batalha */
    private List<BattleEffect> monsterActiveEffects;

    /** Número de ataques garantidos que o monstro deve realizar (usado por skills como Singular Strike do Diabrete) */
    private Integer monsterGuaranteedAttacks = 0;

    /**
     * Classe interna com informações do personagem durante a batalha.
     * Inclui atributos, status e efeitos ativos.
     */
    @Data
    public static class CharacterBattleInfo {
        /** ID do personagem */
        private Integer id;

        /** Pontos de vida atuais */
        private Integer hp;

        /** Pontos de vida máximos */
        private Integer maxHp;

        /** Energia atual */
        private Integer energy;

        /** Energia máxima */
        private Integer maxEnergy;

        /** Nome da classe */
        private String className;

        /** Atributo de força */
        private Integer strength;

        /** Atributo de inteligência */
        private Integer intelligence;

        /** Atributo de defesa */
        private Integer defense;

        /** Nível do personagem */
        private Integer level;

        /** Experiência acumulada */
        private Integer xp;

        /** Experiência máxima necessária para o próximo nível */
        private Integer maxXpForLevel;

        /** Indica se o personagem está defendendo */
        private Boolean isDefending = false;

        /** Efeitos ativos no personagem */
        private Map<String, Object> effects;
    }

    /**
     * Classe interna com informações do monstro durante a batalha.
     */
    @Data
    public static class MonsterBattleInfo {
        /** ID do monstro */
        private Integer id;

        /** Pontos de vida atuais */
        private Integer hp;

        /** Pontos de vida máximos */
        private Integer maxHp;

        /** Dano base do monstro */
        private Integer dano;

        /** Defesa do monstro */
        private Integer defense;

        /** Indica se o monstro está defendendo */
        private Boolean isDefending = false;

        /** Nome do monstro */
        private String nome;
    }

    /**
     * Classe interna com informações da questão atual.
     */
    @Data
    public static class QuestionInfo {
        /** ID da questão */
        private Integer id;

        /** Texto da pergunta */
        private String texto;

        /** Lista de opções de resposta */
        private List<String> opcoes;

        /** Nível mínimo necessário para esta questão */
        private Integer nivelMinimo;

        /** Dificuldade da questão */
        private String difficulty;
    }
}
