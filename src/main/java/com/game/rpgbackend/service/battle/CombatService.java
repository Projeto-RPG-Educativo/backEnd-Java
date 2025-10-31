package com.game.rpgbackend.service.battle;

import com.game.rpgbackend.dto.response.battle.BattleStateResponse;
import com.game.rpgbackend.config.GameConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de combate, cálculo de dano e processamento de turnos.
 */
@Service
@RequiredArgsConstructor
public class CombatService {

    private final GameConfig gameConfig;

    /**
     * Calcula o dano base de um personagem com base em sua classe.
     * @param characterInfo Informações do personagem em batalha.
     * @return O valor do dano.
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
     * Calcula o dano do monstro considerando defesa do personagem.
     * @param monsterDamage Dano base do monstro.
     * @param characterDefense Defesa base do personagem.
     * @param isCharacterDefending Se o personagem está ativamente defendendo.
     * @return Dano final após defesa.
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
     * Calcula o dano do personagem considerando defesa do monstro.
     * @param characterDamage Dano base do personagem.
     * @param monsterDefense Defesa base do monstro.
     * @param isMonsterDefending Se o monstro está ativamente defendendo.
     * @return Dano final após defesa.
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
            character.setEnergy(Math.min(character.getEnergy() + energyRecovered, character.getMaxEnergy()));

            turnResult = String.format("Você acertou! Você recuperou %d de energia.", energyRecovered);

        } else {
            turnResult = "Você errou! Não recuperou energia e perdeu o turno.";
        }

        return new TurnResult(battleState, turnResult, damageDealt, damageTaken);
    }

    /**
     * Aplica o efeito de um ataque.
     * @param character O personagem que ataca.
     * @return Um objeto com o dano causado e uma mensagem.
     */
    public AttackResult performAttack(BattleStateResponse.CharacterBattleInfo character, BattleStateResponse.MonsterBattleInfo monster) {
        int baseDamage = calculateCharacterDamage(character);
        int actualDamage = calculateCharacterDamageWithDefense(baseDamage, monster.getDefense(), monster.getIsDefending());
        String turnResult = String.format("Você atacou! O monstro sofreu %d de dano.", actualDamage);
        // Se o monstro estava defendendo, consumir a defesa
        if (monster.getIsDefending()) {
            monster.setIsDefending(false);
        }
        return new AttackResult(actualDamage, turnResult);
    }

    /**
     * Aplica o efeito de uma defesa.
     * @param character O personagem que defende.
     * @return Um objeto com o novo estado de defesa e uma mensagem.
     */
    public DefenseResult performDefense(BattleStateResponse.CharacterBattleInfo character) {
        character.setIsDefending(true);
        String turnResult = "Você se prepara para o próximo ataque, bloqueando o dano.";
        return new DefenseResult(character, turnResult);
    }

    /**
     * Aplica o efeito da habilidade especial de uma classe.
     * @param character O personagem que usa a habilidade.
     * @return Um objeto com os estados atualizados e uma mensagem do turno.
     */
    public SkillResult performSkill(BattleStateResponse.CharacterBattleInfo character) {
        String turnResult = "";
        SkillEffect effect = null;
        String className = character.getClassName().toLowerCase();

        switch (className) {
            case "paladino":
                int healAmount = gameConfig.getSkills().getPaladino() != null
                    ? gameConfig.getSkills().getPaladino().getHealAmount()
                    : 20;
                character.setHp(character.getHp() + healAmount);
                turnResult = String.format("Você usa Cura e recupera %d de vida!", healAmount);
                break;

            case "tank":
                character.setIsDefending(true);
                turnResult = "Você usa 'Eu Aguento!' e se prepara para bloquear o próximo dano caso erre a resposta.";
                break;

            case "lutador":
                if (character.getEffects() == null) {
                    character.setEffects(new java.util.HashMap<>());
                }
                character.getEffects().put("investidaActive", true);
                turnResult = "Você ativa Investida! Seu próximo acerto causará um golpe extra.";
                break;

            case "mago":
                turnResult = "Você usa Clarividência para prever o futuro...";
                effect = new SkillEffect("REMOVE_WRONG_ANSWER");
                break;

            case "ladino":
                turnResult = "Você tenta encontrar uma fraqueza na pergunta do monstro...";
                effect = new SkillEffect("PROVIDE_HINT");
                break;

            case "bardo":
                turnResult = "Você usa Lábia, preparando uma pergunta de tudo ou nada para terminar o combate!";
                effect = new SkillEffect("BARD_CHALLENGE");
                break;

            default:
                turnResult = String.format("Sua classe (%s) não possui uma habilidade especial implementada.",
                    character.getClassName());
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
        // Escolhe uma ação aleatória: 0 = atacar, 1 = defender, 2 = usar skill
        int action = (int) (Math.random() * 3);
        String turnResult = "";
        int damageDealt = 0;
        String actionStr = "";

        BattleStateResponse.CharacterBattleInfo character = battleState.getCharacter();
        BattleStateResponse.MonsterBattleInfo monster = battleState.getMonster();

        switch (action) {
            case 0: // Atacar
                actionStr = "attack";
                int baseDamage = monster.getDano();
                damageDealt = calculateMonsterDamage(baseDamage, character.getDefense(), character.getIsDefending());
                character.setHp(character.getHp() - damageDealt);
                turnResult = String.format("O monstro atacou! Você sofreu %d de dano.", damageDealt);
                // Se o personagem estava defendendo, consumir a defesa
                if (character.getIsDefending()) {
                    character.setIsDefending(false);
                }
                break;

            case 1: // Defender
                actionStr = "defend";
                monster.setIsDefending(true);
                turnResult = "O monstro se defendeu e reduziu o dano que receberia no turno.";
                break;

            case 2: // Usar skill
                actionStr = "skill";
                // Por enquanto, skill do monstro não implementada, apenas mensagem
                turnResult = "O monstro usou uma habilidade especial!";
                break;
        }

        return new MonsterTurnResult(battleState, turnResult, damageDealt, actionStr);
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
}
