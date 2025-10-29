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
            character.setEnergy(character.getEnergy() + energyRecovered);

            damageDealt = calculateCharacterDamage(character);

            BattleStateResponse.MonsterBattleInfo monster = battleState.getMonster();
            monster.setHp(monster.getHp() - damageDealt);

            turnResult = String.format("Você acertou! O monstro sofreu %d de dano e você recuperou %d de energia.",
                damageDealt, energyRecovered);

            // LÓGICA DA INVESTIDA DO LUTADOR
            if (character.getEffects() != null && Boolean.TRUE.equals(character.getEffects().get("investidaActive"))) {
                int extraHitDamage = calculateCharacterDamage(character);
                monster.setHp(monster.getHp() - extraHitDamage);
                turnResult += String.format(" Sua Investida aplica um golpe extra de %d de dano!", extraHitDamage);
                character.getEffects().remove("investidaActive"); // Consome o efeito
            }

        } else {
            BattleStateResponse.CharacterBattleInfo character = battleState.getCharacter();

            // Se o personagem estava defendendo, não toma dano
            if (Boolean.TRUE.equals(character.getIsDefending())) {
                turnResult = "Você errou, mas sua defesa bloqueou o dano do monstro!";
                character.setIsDefending(false); // Defesa é consumida
            } else {
                damageTaken = battleState.getMonster().getDano();
                character.setHp(character.getHp() - damageTaken);
                turnResult = String.format("Você errou! O monstro te atacou e causou %d de dano.", damageTaken);
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
        int damageDealt = calculateCharacterDamage(character);
        String turnResult = String.format("Você atacou! O monstro sofreu %d de dano.", damageDealt);
        return new AttackResult(damageDealt, turnResult);
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
}

