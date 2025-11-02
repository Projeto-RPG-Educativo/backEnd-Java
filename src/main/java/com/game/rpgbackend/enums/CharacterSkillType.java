package com.game.rpgbackend.enums;

/**
 * Enum que representa as habilidades especiais de cada classe de personagem.
 * <p>
 * Cada classe possui uma habilidade única que pode ser usada durante batalhas,
 * custando energia e proporcionando vantagens estratégicas.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
public enum CharacterSkillType {
    /**
     * Paladino - Cura: Recupera pontos de vida.
     */
    PALADIN_HEAL("Cura", "Você usa Cura e recupera %d de vida!", "HEAL"),

    /**
     * Tank - Eu Aguento!: Prepara-se para bloquear o próximo dano.
     */
    TANK_BLOCK("Eu Aguento!", "Você usa 'Eu Aguento!' e se prepara para bloquear o próximo dano.", "BLOCK"),

    /**
     * Lutador - Investida: Causa 125% de dano se monstro não defender, 115% se defender.
     */
    FIGHTER_CHARGE("Investida", "Você ativa Investida e se prepara para avançar contra o monstro!", "CHARGE"),

    /**
     * Mago - Clarividência: Remove uma opção incorreta da pergunta.
     */
    MAGE_CLAIRVOYANCE("Clarividência", "Você usa Clarividência para prever o futuro...", "REMOVE_WRONG_ANSWER"),

    /**
     * Ladino - Fraqueza: Fornece uma dica sobre a resposta correta.
     */
    ROGUE_WEAKNESS("Fraqueza", "Você tenta encontrar uma fraqueza na pergunta do monstro...", "PROVIDE_HINT"),

    /**
     * Bardo - Lábia: Prepara uma pergunta de tudo ou nada para terminar o combate.
     */
    BARD_CHALLENGE("Lábia", "Você usa Lábia, preparando uma pergunta de tudo ou nada para terminar o combate!", "BARD_CHALLENGE");

    private final String skillName;
    private final String description;
    private final String effectType;

    CharacterSkillType(String skillName, String description, String effectType) {
        this.skillName = skillName;
        this.description = description;
        this.effectType = effectType;
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

    /**
     * Obtém a habilidade especial correspondente ao nome da classe do personagem.
     * <p>
     * Mapeamento de classes para habilidades:
     * - Paladino → Cura
     * - Tank → Eu Aguento!
     * - Lutador → Investida
     * - Mago → Clarividência
     * - Ladino → Fraqueza
     * - Bardo → Lábia
     * </p>
     *
     * @param className nome da classe do personagem (case-insensitive)
     * @return habilidade especial da classe ou null se a classe não for reconhecida
     */
    public static CharacterSkillType getByClassName(String className) {
        if (className == null) return null;

        switch (className.toLowerCase()) {
            case "paladino":
                return PALADIN_HEAL;
            case "tank":
                return TANK_BLOCK;
            case "lutador":
            case "guerreiro":
                return FIGHTER_CHARGE;
            case "mago":
                return MAGE_CLAIRVOYANCE;
            case "ladino":
                return ROGUE_WEAKNESS;
            case "bardo":
                return BARD_CHALLENGE;
            default:
                return null;
        }
    }
}

