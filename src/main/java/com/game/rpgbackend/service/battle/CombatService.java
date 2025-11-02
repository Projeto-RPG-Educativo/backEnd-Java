package com.game.rpgbackend.service.battle;

import com.game.rpgbackend.dto.response.battle.BattleStateResponse;
import com.game.rpgbackend.dto.response.battle.BattleEffect;
import com.game.rpgbackend.config.GameConfig;
import com.game.rpgbackend.enums.CharacterSkillType;
import com.game.rpgbackend.enums.MonsterSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de combate e cálculos de batalha.
 * <p>
 * Gerencia todos os aspectos matemáticos e mecânicos do combate:
 * - Cálculo de dano de personagens e monstros
 * - Aplicação de modificadores de defesa
 * - Processamento de habilidades especiais
 * - Gerenciamento de efeitos de batalha (buffs/debuffs)
 * - Execução de ações de ataque, defesa e skills
 * - Lógica de comportamento dos monstros (IA básica)
 * </p>
 * <p>
 * Os cálculos consideram atributos de classe, nível, equipamentos
 * e efeitos temporários ativos na batalha.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CombatService {

    private final GameConfig gameConfig;

    /**
     * Calcula o dano base do personagem baseado na classe.
     * <p>
     * Classes físicas (Tank, Lutador, Ladino, Paladino) usam Força.
     * Classes mágicas (Mago, Bardo) usam Inteligência.
     * </p>
     *
     * @param characterInfo informações do personagem em batalha
     * @return valor do dano base calculado
     */
    public int calculateCharacterDamage(BattleStateResponse.CharacterBattleInfo characterInfo) {
        String className = characterInfo.getClassName().toLowerCase();

        switch (className) {
            case "tank":
            case "lutador":
            case "ladino":
            case "paladino":
                return characterInfo.getStrength() != null ? characterInfo.getStrength() : 5;
            case "mago":
            case "bardo":
                return characterInfo.getIntelligence() != null ? characterInfo.getIntelligence() : 5;
            default:
                int strength = characterInfo.getStrength() != null ? characterInfo.getStrength() : 5;
                int intelligence = characterInfo.getIntelligence() != null ? characterInfo.getIntelligence() : 5;
                return Math.max(strength, intelligence);
        }
    }

    /**
     * Calcula o dano que o monstro causa ao personagem após mitigação por defesa.
     * <p>
     * Fórmula de redução:
     * - Defesa passiva: reduz 1% de dano a cada 2 pontos de defesa
     * - Defesa ativa (ação defender): reduz 50% adicional do dano resultante
     * </p>
     *
     * @param monsterDamage dano base do monstro
     * @param characterDefense valor de defesa do personagem
     * @param isCharacterDefending true se o personagem usou a ação "defender"
     * @return dano final que será aplicado ao HP do personagem (mínimo 0)
     */
    public int calculateMonsterDamage(int monsterDamage, int characterDefense, boolean isCharacterDefending) {
        // Reduz 1% de dano por 2 pontos de defesa base
        int reductionPercent = (characterDefense / 2);
        int reducedDamage = monsterDamage * (100 - reductionPercent) / 100;

        // Se o personagem está defendendo, reduzir mais 50%
        if (isCharacterDefending) {
            reducedDamage = reducedDamage / 2;
        }

        return Math.max(reducedDamage, 0); // Dano mínimo 0
    }

    /**
     * Calcula o dano que o personagem causa ao monstro após mitigação por defesa.
     * <p>
     * Fórmula de redução:
     * - Defesa passiva: reduz 1% de dano a cada 2 pontos de defesa
     * - Defesa ativa (monstro defendendo): reduz 50% adicional do dano resultante
     * </p>
     *
     * @param characterDamage dano base do personagem
     * @param monsterDefense valor de defesa do monstro
     * @param isMonsterDefending true se o monstro está em postura defensiva
     * @return dano final que será aplicado ao HP do monstro (mínimo 0)
     */
    public int calculateCharacterDamageWithDefense(int characterDamage, int monsterDefense, boolean isMonsterDefending) {
        // Reduz 1% de dano por 2 pontos de defesa base do monstro
        int reductionPercent = (monsterDefense / 2);
        int reducedDamage = characterDamage * (100 - reductionPercent) / 100;

        // Se o monstro está defendendo, reduzir mais 50%
        if (isMonsterDefending) {
            reducedDamage = reducedDamage / 2;
        }

        return Math.max(reducedDamage, 0); // Dano mínimo 0
    }

    /**
     * Calcula o dano do personagem considerando defesa do monstro E efeitos ativos.
     * @param battleState Estado da batalha com efeitos ativos.
     * @param characterDamage Dano base do personagem.
     * @param monsterDefense Defesa base do monstro.
     * @param isMonsterDefending Se o monstro está ativamente defendendo.
     * @return Dano final após defesa e efeitos.
     */
    public int calculateCharacterDamageWithDefenseAndEffects(BattleStateResponse battleState, int characterDamage, int monsterDefense, boolean isMonsterDefending) {
        int reducedDamage = calculateCharacterDamageWithDefense(characterDamage, monsterDefense, isMonsterDefending);

        // Verifica se o monstro tem redução de dano ativa (ex: Ignorance do Zumbi)
        if (hasMonsterEffect(battleState, "DAMAGE_REDUCTION")) {
            for (BattleEffect effect : battleState.getMonsterActiveEffects()) {
                if ("DAMAGE_REDUCTION".equals(effect.getType()) && effect.isActive()) {
                    reducedDamage = reducedDamage * (100 - effect.getMagnitude()) / 100;
                    break;
                }
            }
        }

        return Math.max(reducedDamage, 0); // Dano mínimo 0
    }

    /**
     * Processa o turno do jogador baseado na resposta a uma pergunta.
     * @param battleState O estado atual da batalha.
     * @param isCorrect Se a resposta do jogador foi correta.
     * @return Um objeto com o resultado do turno.
     */
    public TurnResult processAnswerTurn(BattleStateResponse battleState, boolean isCorrect) {
        int damageDealt = 0;
        int damageTaken = 0;
        String turnResult = "";

        int energyRecovered = gameConfig.getBattle().getEnergyRecovery();

        if (isCorrect) {
            BattleStateResponse.CharacterBattleInfo character = battleState.getCharacter();

            // Verifica se a recuperação de energia está bloqueada (Syllable Scramble do Malak)
            if (hasCharacterEffect(battleState, "BLOCK_ENERGY_RECOVERY")) {
                turnResult = "Você acertou, mas a habilidade do monstro impede a recuperação de energia!";
            } else {
                character.setEnergy(Math.min(character.getEnergy() + energyRecovered, character.getMaxEnergy()));
                turnResult = String.format("Você acertou! Você recuperou %d de energia.", energyRecovered);
            }

        } else {
            // Verifica se há efeito de drenagem de vida (Semantic Drain do Lexicógrafo)
            if (hasCharacterEffect(battleState, "CORRUPTION")) {
                // Pega a magnitude do efeito
                int lifeDrain = 0;
                if (battleState.getCharacterActiveEffects() != null) {
                    for (BattleEffect effect : battleState.getCharacterActiveEffects()) {
                        if ("CORRUPTION".equals(effect.getType()) && effect.isActive()) {
                            lifeDrain = effect.getMagnitude();
                            break;
                        }
                    }
                }

                BattleStateResponse.CharacterBattleInfo character = battleState.getCharacter();
                character.setHp(character.getHp() - lifeDrain);
                turnResult = String.format("Você errou! O efeito de corrupção drena %d de vida. Não recuperou energia e perdeu o turno.", lifeDrain);
            } else {
                turnResult = "Você errou! Não recuperou energia e perdeu o turno.";
            }
        }

        return new TurnResult(battleState, turnResult, damageDealt, damageTaken);
    }

    /**
     * Aplica o efeito de um ataque.
     * @param character O personagem que ataca.
     * @return Um objeto com o dano causado e uma mensagem.
     */
    public AttackResult performAttack(BattleStateResponse.CharacterBattleInfo character) {
        int baseDamage = calculateCharacterDamage(character);
        // Nota: o dano será aplicado APÓS o turno do monstro, então não verificamos defesa aqui ainda
        String turnResult = "Você preparou um ataque! Aguardando resposta do monstro...";
        return new AttackResult(baseDamage, turnResult);
    }

    /**
     * Aplica o efeito de uma defesa.
     * @param character O personagem que defende.
     * @return Um objeto com o novo estado de defesa e uma mensagem.
     */
    public DefenseResult performDefense(BattleStateResponse.CharacterBattleInfo character) {
        character.setIsDefending(true);
        String turnResult = "Você assumiu uma postura defensiva! O dano recebido será reduzido em 50%.";
        return new DefenseResult(character, turnResult);
    }

    /**
     * Aplica o efeito da habilidade especial de uma classe usando o enum CharacterSkillType.
     * @param character O personagem que usa a habilidade.
     * @return Um objeto com os estados atualizados e uma mensagem do turno.
     */
    public SkillResult performSkill(BattleStateResponse.CharacterBattleInfo character) {
        String turnResult = "";
        SkillEffect effect = null;

        CharacterSkillType skillType = CharacterSkillType.getByClassName(character.getClassName());

        if (skillType == null) {
            turnResult = String.format("Sua classe (%s) não possui uma habilidade especial implementada.",
                character.getClassName());
            return new SkillResult(character, turnResult, effect);
        }

        switch (skillType) {
            case PALADIN_HEAL:
                int healAmount = gameConfig.getSkills().getPaladino() != null
                    ? gameConfig.getSkills().getPaladino().getHealAmount()
                    : 20;
                int newHp = Math.min(character.getHp() + healAmount, character.getMaxHp());
                character.setHp(newHp);
                turnResult = String.format(skillType.getDescription(), healAmount);
                break;

            case TANK_BLOCK:
                character.setIsDefending(true);
                turnResult = skillType.getDescription();
                break;

            case FIGHTER_CHARGE:
                // Investida funciona como um ataque, mas com dano variável:
                // 125% se monstro NÃO estiver defendendo
                // 115% se monstro estiver defendendo
                int baseDamage = calculateCharacterDamage(character);

                // Marca que há dano pendente (será aplicado após turno do monstro)
                if (character.getEffects() == null) {
                    character.setEffects(new java.util.HashMap<>());
                }
                character.getEffects().put("chargeBaseDamage", baseDamage);
                character.getEffects().put("isChargeActive", true);

                turnResult = "Você ativa Investida e se prepara para avançar contra o monstro!";
                break;

            case MAGE_CLAIRVOYANCE:
                turnResult = skillType.getDescription();
                effect = new SkillEffect(skillType.getEffectType());
                break;

            case ROGUE_WEAKNESS:
                turnResult = skillType.getDescription();
                effect = new SkillEffect(skillType.getEffectType());
                break;

            case BARD_CHALLENGE:
                turnResult = skillType.getDescription();
                effect = new SkillEffect(skillType.getEffectType());
                break;
        }

        return new SkillResult(character, turnResult, effect);
    }

    /**
     * Executa a ação aleatória do monstro no seu turno.
     * @param battleState O estado atual da batalha.
     * @return Um objeto com o resultado da ação do monstro.
     */
    public MonsterTurnResult performMonsterTurn(BattleStateResponse battleState) {
        String turnResult = "";
        int damageDealt = 0;
        String actionStr = "";

        BattleStateResponse.CharacterBattleInfo character = battleState.getCharacter();
        BattleStateResponse.MonsterBattleInfo monster = battleState.getMonster();

        // Verifica se o monstro tem ataques garantidos (ex: após usar Singular Strike)
        int action;
        if (battleState.getMonsterGuaranteedAttacks() != null && battleState.getMonsterGuaranteedAttacks() > 0) {
            // Força ataque e decrementa o contador
            action = 0; // 0 = atacar
            battleState.setMonsterGuaranteedAttacks(battleState.getMonsterGuaranteedAttacks() - 1);
        } else {
            // Escolhe uma ação aleatória: 0 = atacar, 1 = defender, 2 = usar skill
            action = (int) (Math.random() * 3);
        }

        switch (action) {
            case 0: // Atacar
                actionStr = "attack";
                int baseDamage = monster.getDano();

                // Verifica se o monstro tem buff de dano ativo (ex: Singular Strike do Diabrete)
                if (battleState.getMonsterActiveEffects() != null) {
                    for (BattleEffect effect : battleState.getMonsterActiveEffects()) {
                        if ("DAMAGE_BUFF".equals(effect.getType()) && effect.isActive()) {
                            baseDamage = baseDamage + (baseDamage * effect.getMagnitude() / 100);
                            turnResult = "O monstro ataca com força aumentada! ";
                            break;
                        }
                    }
                }

                damageDealt = calculateMonsterDamage(baseDamage, character.getDefense(), character.getIsDefending());
                character.setHp(character.getHp() - damageDealt);

                // Mensagem com informações sobre mitigação
                if (character.getIsDefending()) {
                    int damageWithoutDefense = calculateMonsterDamage(baseDamage, character.getDefense(), false);
                    turnResult += String.format("O monstro atacou! Você bloqueou parte do ataque e sofreu apenas %d de dano (de %d).",
                        damageDealt, damageWithoutDefense);
                    character.setIsDefending(false);
                } else {
                    turnResult += String.format("O monstro atacou! Você sofreu %d de dano.", damageDealt);
                }
                break;

            case 1: // Defender
                actionStr = "defend";
                monster.setIsDefending(true);
                turnResult = "O monstro assumiu postura defensiva! O próximo dano será reduzido em 50%.";
                break;

            case 2: // Usar skill
                actionStr = "skill";
                MonsterSkillResult skillResult = performMonsterSkill(battleState);
                if (skillResult != null) {
                    turnResult = skillResult.getTurnResult();
                    // O efeito foi adicionado à lista de efeitos ativos no método performMonsterSkill
                } else {
                    turnResult = "O monstro tentou usar uma habilidade, mas falhou!";
                }
                break;
        }

        return new MonsterTurnResult(battleState, turnResult, damageDealt, actionStr);
    }

    /**
     * Executa a habilidade especial de um monstro baseado no seu nome.
     * @param battleState O estado atual da batalha.
     * @return Um objeto com o resultado da skill do monstro.
     */
    public MonsterSkillResult performMonsterSkill(BattleStateResponse battleState) {
        BattleStateResponse.MonsterBattleInfo monster = battleState.getMonster();

        MonsterSkillType skillType = MonsterSkillType.getSkillByMonsterName(monster.getNome());

        if (skillType == null) {
            return new MonsterSkillResult("O monstro não possui habilidade especial!", null);
        }

        String turnResult = skillType.getDescription();
        BattleEffect effect = null;

        switch (skillType) {
            case DIABRETE_SINGULAR_STRIKE:
                // Adiciona buff de dano por 2 turnos
                effect = new BattleEffect(
                    skillType.getEffectType(),
                    skillType.getMagnitude(),
                    skillType.getDuration(),
                    "Dano aumentado em " + skillType.getMagnitude() + "%"
                );
                addEffectToMonster(battleState, effect);
                // Garante que as próximas 2 ações sejam ataques para maximizar o uso do buff
                battleState.setMonsterGuaranteedAttacks(2);
                break;

            case HARPIA_WHIRLWIND_QUESTION:
                // 50% de chance de embaralhar a próxima pergunta
                boolean scrambled = Math.random() < 0.5;
                if (scrambled) {
                    effect = new BattleEffect(
                        skillType.getEffectType(),
                        skillType.getMagnitude(),
                        skillType.getDuration(),
                        "As palavras da próxima pergunta estão em desordem!"
                    );
                    addEffectToCharacter(battleState, effect);
                    turnResult = String.format(skillType.getDescription(), "As palavras da próxima pergunta estão em desordem! Reordene-as para encontrar a resposta correta.");
                } else {
                    turnResult = String.format(skillType.getDescription(), "Você conseguiu resistir ao efeito!");
                }
                break;

            case ZUMBI_IGNORANCE:
                // Reduz dano recebido do próximo ataque em 50%
                effect = new BattleEffect(
                    skillType.getEffectType(),
                    skillType.getMagnitude(),
                    skillType.getDuration(),
                    "Dano recebido reduzido em " + skillType.getMagnitude() + "%"
                );
                addEffectToMonster(battleState, effect);
                break;

            case ESQUELETO_SYNTAX_COLLAPSE:
                // Atordoa o jogador por 1 turno
                effect = new BattleEffect(
                    skillType.getEffectType(),
                    skillType.getMagnitude(),
                    skillType.getDuration(),
                    "Você está atordoado!"
                );
                addEffectToCharacter(battleState, effect);
                break;

            case CENTAURO_WH_QUESTION_VOLLEY:
                // Marca para lançar uma pergunta extra em tempo limitado
                effect = new BattleEffect(
                    skillType.getEffectType(),
                    skillType.getMagnitude(),
                    skillType.getDuration(),
                    "Pergunta extra em tempo limitado!"
                );
                addEffectToCharacter(battleState, effect);
                break;

            case LEXICOGRAFO_SEMANTIC_DRAIN:
                // Corrompe significados - erros drenam vida
                effect = new BattleEffect(
                    skillType.getEffectType(),
                    skillType.getMagnitude(),
                    skillType.getDuration(),
                    "Erros nas respostas drenarão sua vida!"
                );
                addEffectToCharacter(battleState, effect);
                break;

            case MALAK_AMNESIA_BLAST:
                // Impede uso de habilidades de classe
                effect = new BattleEffect(
                    skillType.getEffectType(),
                    skillType.getMagnitude(),
                    skillType.getDuration(),
                    "Habilidades de classe desabilitadas!"
                );
                addEffectToCharacter(battleState, effect);
                break;

            case MALAK_SYLLABLE_SCRAMBLE:
                // Impede recuperação de energia via perguntas
                effect = new BattleEffect(
                    skillType.getEffectType(),
                    skillType.getMagnitude(),
                    skillType.getDuration(),
                    "Recuperação de energia bloqueada!"
                );
                addEffectToCharacter(battleState, effect);
                break;

            case MALAK_LEXICAL_BLINDNESS:
                // Esconde a próxima pergunta
                effect = new BattleEffect(
                    skillType.getEffectType(),
                    skillType.getMagnitude(),
                    skillType.getDuration(),
                    "A próxima pergunta será ocultada!"
                );
                addEffectToCharacter(battleState, effect);
                break;
        }

        return new MonsterSkillResult(turnResult, effect);
    }

    /**
     * Adiciona um efeito à lista de efeitos ativos do personagem.
     */
    private void addEffectToCharacter(BattleStateResponse battleState, BattleEffect effect) {
        if (battleState.getCharacterActiveEffects() == null) {
            battleState.setCharacterActiveEffects(new java.util.ArrayList<>());
        }
        battleState.getCharacterActiveEffects().add(effect);
    }

    /**
     * Adiciona um efeito à lista de efeitos ativos do monstro.
     */
    private void addEffectToMonster(BattleStateResponse battleState, BattleEffect effect) {
        if (battleState.getMonsterActiveEffects() == null) {
            battleState.setMonsterActiveEffects(new java.util.ArrayList<>());
        }
        battleState.getMonsterActiveEffects().add(effect);
    }

    /**
     * Decrementa a duração de todos os efeitos ativos e remove os expirados.
     */
    public void updateActiveEffects(BattleStateResponse battleState) {
        // Atualiza efeitos do personagem
        if (battleState.getCharacterActiveEffects() != null) {
            battleState.getCharacterActiveEffects().removeIf(effect -> !effect.decrementDuration());
        }

        // Atualiza efeitos do monstro
        if (battleState.getMonsterActiveEffects() != null) {
            // Para o buff de dano do Diabrete (DAMAGE_BUFF), só decrementa se não houver mais ataques garantidos
            // Isso garante que o buff dure exatamente 2 ataques
            battleState.getMonsterActiveEffects().removeIf(effect -> {
                // Se for buff de dano e ainda houver ataques garantidos, não decrementa
                if ("DAMAGE_BUFF".equals(effect.getType()) &&
                    battleState.getMonsterGuaranteedAttacks() != null &&
                    battleState.getMonsterGuaranteedAttacks() > 0) {
                    return false; // Mantém o efeito sem decrementar
                }
                // Para outros efeitos, decrementa normalmente
                return !effect.decrementDuration();
            });
        }
    }

    /**
     * Verifica se um efeito específico está ativo no personagem.
     */
    public boolean hasCharacterEffect(BattleStateResponse battleState, String effectType) {
        if (battleState.getCharacterActiveEffects() == null) return false;
        return battleState.getCharacterActiveEffects().stream()
            .anyMatch(effect -> effectType.equals(effect.getType()) && effect.isActive());
    }

    /**
     * Verifica se um efeito específico está ativo no monstro.
     */
    public boolean hasMonsterEffect(BattleStateResponse battleState, String effectType) {
        if (battleState.getMonsterActiveEffects() == null) return false;
        return battleState.getMonsterActiveEffects().stream()
            .anyMatch(effect -> effectType.equals(effect.getType()) && effect.isActive());
    }

    // Classes internas para resultados
    public static class TurnResult {
        private final BattleStateResponse updatedBattleState;
        private final String turnResult;
        private final int damageDealt;
        private final int damageTaken;

        public TurnResult(BattleStateResponse updatedBattleState, String turnResult, int damageDealt, int damageTaken) {
            this.updatedBattleState = updatedBattleState;
            this.turnResult = turnResult;
            this.damageDealt = damageDealt;
            this.damageTaken = damageTaken;
        }

        public BattleStateResponse getUpdatedBattleState() { return updatedBattleState; }
        public String getTurnResult() { return turnResult; }
        public int getDamageDealt() { return damageDealt; }
        public int getDamageTaken() { return damageTaken; }
    }

    public static class AttackResult {
        private final int damageDealt;
        private final String turnResult;

        public AttackResult(int damageDealt, String turnResult) {
            this.damageDealt = damageDealt;
            this.turnResult = turnResult;
        }

        public int getDamageDealt() { return damageDealt; }
        public String getTurnResult() { return turnResult; }
    }

    public static class DefenseResult {
        private final BattleStateResponse.CharacterBattleInfo updatedCharacter;
        private final String turnResult;

        public DefenseResult(BattleStateResponse.CharacterBattleInfo updatedCharacter, String turnResult) {
            this.updatedCharacter = updatedCharacter;
            this.turnResult = turnResult;
        }

        public BattleStateResponse.CharacterBattleInfo getUpdatedCharacter() { return updatedCharacter; }
        public String getTurnResult() { return turnResult; }
    }

    public static class SkillResult {
        private final BattleStateResponse.CharacterBattleInfo updatedCharacter;
        private final String turnResult;
        private final SkillEffect effect;

        public SkillResult(BattleStateResponse.CharacterBattleInfo updatedCharacter, String turnResult, SkillEffect effect) {
            this.updatedCharacter = updatedCharacter;
            this.turnResult = turnResult;
            this.effect = effect;
        }

        public BattleStateResponse.CharacterBattleInfo getUpdatedCharacter() { return updatedCharacter; }
        public String getTurnResult() { return turnResult; }
        public SkillEffect getEffect() { return effect; }
    }

    public static class SkillEffect {
        private final String type;

        public SkillEffect(String type) {
            this.type = type;
        }

        public String getType() { return type; }
    }

    public static class MonsterTurnResult {
        private final BattleStateResponse updatedBattleState;
        private final String turnResult;
        private final int damageDealt;
        private final String action;

        public MonsterTurnResult(BattleStateResponse updatedBattleState, String turnResult, int damageDealt, String action) {
            this.updatedBattleState = updatedBattleState;
            this.turnResult = turnResult;
            this.damageDealt = damageDealt;
            this.action = action;
        }

        public BattleStateResponse getUpdatedBattleState() { return updatedBattleState; }
        public String getTurnResult() { return turnResult; }
        public int getDamageDealt() { return damageDealt; }
        public String getAction() { return action; }
    }

    public static class MonsterSkillResult {
        private final String turnResult;
        private final BattleEffect effect;

        public MonsterSkillResult(String turnResult, BattleEffect effect) {
            this.turnResult = turnResult;
            this.effect = effect;
        }

        public String getTurnResult() { return turnResult; }
        public BattleEffect getEffect() { return effect; }
    }
}
