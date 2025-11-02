package com.game.rpgbackend.dto.response.battle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa um efeito ativo durante a batalha.
 * <p>
 * Efeitos podem ser buffs, debuffs ou condições especiais que afetam o combate
 * por um número determinado de turnos.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleEffect {
    /**
     * Tipo do efeito (DAMAGE_BUFF, DAMAGE_REDUCTION, STUN, etc.)
     */
    private String type;

    /**
     * Magnitude do efeito (porcentagem de buff/debuff, quantidade de dano, etc.)
     */
    private int magnitude;

    /**
     * Duração restante em turnos
     */
    private int duration;

    /**
     * Descrição do efeito para exibição
     */
    private String description;

    /**
     * Decrementa a duração do efeito em 1 turno.
     * @return true se o efeito ainda está ativo, false se expirou
     */
    public boolean decrementDuration() {
        duration--;
        return duration > 0;
    }

    /**
     * Verifica se o efeito ainda está ativo.
     * @return true se duration > 0
     */
    public boolean isActive() {
        return duration > 0;
    }
}

