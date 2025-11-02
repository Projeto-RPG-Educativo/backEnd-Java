package com.game.rpgbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configurações centralizadas do jogo carregadas do application.properties.
 * <p>
 * Centraliza todos os valores configuráveis do jogo para facilitar o balanceamento
 * sem necessidade de recompilar o código. As configurações são divididas em categorias:
 * - Custos de energia para ações de batalha
 * - Recompensas e mecânicas de batalha
 * - Sistema de progressão de nível (leveling)
 * - Parâmetros de habilidades especiais de cada classe
 * </p>
 * <p>
 * Todas as propriedades podem ser sobrescritas no arquivo application.properties
 * usando o prefixo "game." (ex: game.costs.attack=3).
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "game")
@Data
public class GameConfig {

    /** Custos de energia para ações de combate */
    private Costs costs = new Costs();

    /** Configurações de recompensas e mecânicas de batalha */
    private Battle battle = new Battle();

    /** Configurações do sistema de progressão de nível */
    private Leveling leveling = new Leveling();

    /** Parâmetros das habilidades especiais de cada classe */
    private Skills skills = new Skills();

    /**
     * Custos de energia para diferentes ações durante o combate.
     * <p>
     * A energia é um recurso limitado que o jogador deve gerenciar
     * estrategicamente durante as batalhas.
     * </p>
     */
    @Data
    public static class Costs {
        /** Custo de energia para realizar um ataque básico */
        private int attack = 2;

        /** Custo de energia para entrar em postura defensiva */
        private int defend = 1;

        /** Custo de energia para usar habilidade especial da classe */
        private int ability = 3;
    }

    /**
     * Configurações relacionadas ao sistema de batalhas.
     * <p>
     * Define recompensas, limites e mecânicas de energia durante o combate.
     * </p>
     */
    @Data
    public static class Battle {
        /** Quantidade de XP concedida ao vencer uma batalha */
        private int xpWinReward = 50;

        /** Quantidade de energia recuperada ao responder uma questão corretamente */
        private int energyRecovery = 1;

        /** Quantidade máxima de energia que um personagem pode ter */
        private int maxEnergy = 12;
    }

    /**
     * Configurações do sistema de progressão de nível.
     * <p>
     * Define a fórmula de cálculo de XP necessária para subir de nível:
     * XP_necessária = baseXp × (nível ^ xpMultiplier)
     * </p>
     */
    @Data
    public static class Leveling {
        /** XP base necessária para o primeiro nível */
        private int baseXp = 100;

        /** Multiplicador exponencial de XP por nível (progressão) */
        private double xpMultiplier = 1.5;
    }

    /**
     * Parâmetros das habilidades especiais de todas as classes jogáveis.
     * <p>
     * Cada classe possui valores específicos para balanceamento de sua habilidade única.
     * </p>
     */
    @Data
    public static class Skills {
        /** Parâmetros da habilidade do Mago */
        private MagoSkill mago = new MagoSkill();

        /** Parâmetros da habilidade do Tank */
        private TankSkill tank = new TankSkill();

        /** Parâmetros da habilidade do Lutador */
        private LutadorSkill lutador = new LutadorSkill();

        /** Parâmetros da habilidade do Paladino */
        private PaladinoSkill paladino = new PaladinoSkill();

        /** Parâmetros da habilidade do Bardo */
        private BardoSkill bardo = new BardoSkill();

        /** Parâmetros da habilidade do Ladino */
        private LadinoSkill ladino = new LadinoSkill();

        /**
         * Configurações da habilidade "Clarividência" do Mago.
         * Remove uma opção incorreta da questão.
         */
        @Data
        public static class MagoSkill {
            /** Multiplicador de dano da habilidade mágica */
            private int damageMultiplier = 2;
        }

        /**
         * Configurações da habilidade "Eu Aguento!" do Tank.
         * Prepara-se para bloquear o próximo dano.
         */
        @Data
        public static class TankSkill {
            /** Quantidade de HP recuperada pela habilidade */
            private int healAmount = 15;
        }

        /**
         * Configurações da habilidade "Investida" do Lutador.
         * Causa 125% de dano se monstro não defender, 115% se defender.
         */
        @Data
        public static class LutadorSkill {
            /** Dano bônus adicional causado pela habilidade */
            private int bonusDamage = 10;
        }

        /**
         * Configurações da habilidade "Cura" do Paladino.
         * Recupera pontos de vida do personagem.
         */
        @Data
        public static class PaladinoSkill {
            /** Quantidade de HP recuperada pela cura divina */
            private int healAmount = 10;
        }

        /**
         * Configurações da habilidade "Lábia" do Bardo.
         * Prepara uma pergunta de tudo ou nada para terminar o combate.
         */
        @Data
        public static class BardoSkill {
            /** Quantidade de HP recuperada pelo canto inspirador */
            private int healAmount = 8;
        }

        /**
         * Configurações da habilidade "Fraqueza" do Ladino.
         * Fornece uma dica sobre a resposta correta da questão.
         */
        @Data
        public static class LadinoSkill {
            /** Dano bônus do ataque furtivo */
            private int bonusDamage = 8;
        }
    }
}

