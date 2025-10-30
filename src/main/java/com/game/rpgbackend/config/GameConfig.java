package com.game.rpgbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configurações do jogo carregadas do application.properties.
 */
@Configuration
@ConfigurationProperties(prefix = "game")
@Data
public class GameConfig {

    private Costs costs = new Costs();
    private Battle battle = new Battle();
    private Leveling leveling = new Leveling();
    private Skills skills = new Skills();

    @Data
    public static class Costs {
        private int attack = 2;
        private int defend = 1;
        private int ability = 3;
    }

    @Data
    public static class Battle {
        private int xpWinReward = 50;
        private int energyRecovery = 1;
        private int maxEnergy = 12;
    }

    @Data
    public static class Leveling {
        private int baseXp = 100;
        private double xpMultiplier = 1.5;
    }

    @Data
    public static class Skills {
        private MagoSkill mago = new MagoSkill();
        private TankSkill tank = new TankSkill();
        private LutadorSkill lutador = new LutadorSkill();
        private PaladinoSkill paladino = new PaladinoSkill();
        private BardoSkill bardo = new BardoSkill();
        private LadinoSkill ladino = new LadinoSkill();

        @Data
        public static class MagoSkill {
            private int damageMultiplier = 2;
        }

        @Data
        public static class TankSkill {
            private int healAmount = 15;
        }

        @Data
        public static class LutadorSkill {
            private int bonusDamage = 10;
        }

        @Data
        public static class PaladinoSkill {
            private int healAmount = 10;
        }

        @Data
        public static class BardoSkill {
            private int healAmount = 8;
        }

        @Data
        public static class LadinoSkill {
            private int bonusDamage = 8;
        }
    }
}

