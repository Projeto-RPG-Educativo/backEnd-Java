package com.game.rpgbackend.dto.response.hub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para habilidades disponíveis na Torre do Conhecimento.
 * <p>
 * Representa uma habilidade especial que pode ser desbloqueada pelo jogador
 * usando pontos de habilidade ganhos ao subir de nível. Skills oferecem
 * vantagens permanentes no gameplay.
 * </p>
 * <p>
 * Tipos de habilidades:
 * - Passivas: Efeitos sempre ativos (ex: +10% HP máximo)
 * - Ativas: Podem ser usadas em batalha (ex: golpe especial)
 * - Utilitárias: Melhorias fora de combate (ex: +5% ouro ganho)
 * </p>
 * <p>
 * Usado no frontend para:
 * - Exibir catálogo de skills disponíveis
 * - Mostrar custo e efeito de cada skill
 * - Permitir compra com pontos de habilidade
 * - Destacar skills já desbloqueadas
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {

    /**
     * Identificador único da habilidade.
     */
    private Integer id;

    /**
     * Nome da habilidade.
     */
    private String name;

    /**
     * Descrição detalhada da habilidade e como funciona.
     */
    private String description;

    /**
     * Custo em pontos de habilidade para desbloquear.
     */
    private Integer cost;

    /**
     * Tipo da habilidade (passiva, ativa, utilitária).
     */
    private String type;

    /**
     * Efeito da habilidade quando ativa (descrição técnica).
     */
    private String effect;
}
