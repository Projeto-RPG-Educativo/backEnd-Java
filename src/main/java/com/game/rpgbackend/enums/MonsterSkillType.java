package com.game.rpgbackend.enums;

/**
 * Enum que representa as habilidades especiais dos monstros no sistema de batalha.
 * <p>
 * Cada monstro possui habilidades únicas que podem ser usadas durante batalhas,
 * proporcionando desafios estratégicos aos jogadores.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
public enum MonsterSkillType {
    /**
     * Diabrete Errôneo - Singular Strike: Aumenta o dano em 50% pelas próximas 2 rodadas.
     */
    DIABRETE_SINGULAR_STRIKE(
        "Singular Strike",
        "O Diabrete Errôneo usa Singular Strike! Seu dano aumentará em 50% pelas próximas 2 rodadas!",
        "DAMAGE_BUFF",
        50,
        2
    ),

    /**
     * Harpia Indagada - Whirlwind Question: Causa dano padrão e tem 50% de chance de embaralhar as palavras da próxima pergunta.
     */
    HARPIA_WHIRLWIND_QUESTION(
        "Whirlwind Question",
        "A Harpia solta um grito que desorienta! %s",
        "SCRAMBLE_QUESTION",
        50,
        1
    ),

    /**
     * Zumbi Demente - Ignorance: Sua cabeça dura reduz o dano recebido do próximo ataque em 50%.
     */
    ZUMBI_IGNORANCE(
        "Ignorance",
        "O Zumbi Demente usa Ignorance! Sua cabeça dura reduzirá o dano do próximo ataque em 50%.",
        "DAMAGE_REDUCTION",
        50,
        1
    ),

    /**
     * Esqueleto da Sintaxe - Syntax Collapse: Causa dano padrão e atordoa o jogador por 1 turno.
     */
    ESQUELETO_SYNTAX_COLLAPSE(
        "Syntax Collapse",
        "O Esqueleto usa Syntax Collapse! Você está atordoado e não poderá agir no próximo turno!",
        "STUN",
        0,
        1
    ),

    /**
     * Centauro Questionador - Wh-Question Volley: Lança uma segunda pergunta que deve ser respondida em tempo limitado.
     */
    CENTAURO_WH_QUESTION_VOLLEY(
        "Wh-Question Volley",
        "O Centauro Questionador lança uma Wh-Question Volley! Uma segunda pergunta está chegando em tempo limitado!",
        "EXTRA_QUESTION",
        0,
        1
    ),

    /**
     * Lexicógrafo - Semantic Drain: Tenta corromper o significado das palavras. Erros drenam vida e reduzem dano em 10%.
     */
    LEXICOGRAFO_SEMANTIC_DRAIN(
        "Semantic Drain",
        "O Lexicógrafo usa Semantic Drain! Os significados das palavras estão corrompidos. Erros drenarão sua vida!",
        "CORRUPTION",
        10,
        3
    ),

    /**
     * Malak - Amnesia Blast: Impede o uso de habilidades de classe no próximo turno.
     */
    MALAK_AMNESIA_BLAST(
        "Amnesia Blast",
        "Malak usa Amnesia Blast! Você não poderá usar habilidades de classe no próximo turno!",
        "DISABLE_SKILL",
        0,
        1
    ),

    /**
     * Malak - Syllable Scramble: Impede que o jogador responda uma pergunta para recuperar energia.
     */
    MALAK_SYLLABLE_SCRAMBLE(
        "Syllable Scramble",
        "Malak usa Syllable Scramble! Você não poderá responder perguntas para recuperar energia neste turno!",
        "BLOCK_ENERGY_RECOVERY",
        0,
        1
    ),

    /**
     * Malak - Lexical Blindness: Esconde a próxima pergunta, forçando o jogador a chutar a resposta.
     */
    MALAK_LEXICAL_BLINDNESS(
        "Lexical Blindness",
        "Malak usa Lexical Blindness! A próxima pergunta será ocultada. Você terá que adivinhar!",
        "HIDE_QUESTION",
        0,
        1
    );

    private final String skillName;
    private final String description;
    private final String effectType;
    private final int magnitude; // Magnitude do efeito (porcentagem, quantidade, etc.)
    private final int duration; // Duração em turnos

    MonsterSkillType(String skillName, String description, String effectType, int magnitude, int duration) {
        this.skillName = skillName;
        this.description = description;
        this.effectType = effectType;
        this.magnitude = magnitude;
        this.duration = duration;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getDescription() {
        return description;
    }

    public String getEffectType() {
        return effectType;
    }

    public int getMagnitude() {
        return magnitude;
    }

    public int getDuration() {
        return duration;
    }

    /**
     * Obtém uma skill aleatória para o monstro baseado no seu nome.
     * @param monsterName Nome do monstro
     * @return A skill correspondente ou null se não encontrada
     */
    public static MonsterSkillType getSkillByMonsterName(String monsterName) {
        if (monsterName == null) return null;

        String lowerName = monsterName.toLowerCase();

        // Diabrete Errôneo
        if (lowerName.contains("diabrete")) {
            return DIABRETE_SINGULAR_STRIKE;
        }
        // Harpia Indagada
        else if (lowerName.contains("harpia")) {
            return HARPIA_WHIRLWIND_QUESTION;
        }
        // Zumbi Demente
        else if (lowerName.contains("zumbi")) {
            return ZUMBI_IGNORANCE;
        }
        // Esqueleto da Sintaxe
        else if (lowerName.contains("esqueleto")) {
            return ESQUELETO_SYNTAX_COLLAPSE;
        }
        // Centauro Questionador
        else if (lowerName.contains("centauro")) {
            return CENTAURO_WH_QUESTION_VOLLEY;
        }
        // Lexicógrafo
        else if (lowerName.contains("lexicografo") || lowerName.contains("lexicógrafo")) {
            return LEXICOGRAFO_SEMANTIC_DRAIN;
        }
        // Malak - tem múltiplas skills, retorna uma aleatória
        else if (lowerName.contains("malak")) {
            int random = (int) (Math.random() * 3);
            switch (random) {
                case 0: return MALAK_AMNESIA_BLAST;
                case 1: return MALAK_SYLLABLE_SCRAMBLE;
                case 2: return MALAK_LEXICAL_BLINDNESS;
                default: return MALAK_AMNESIA_BLAST;
            }
        }

        return null; // Monstro sem skill definida
    }
}

