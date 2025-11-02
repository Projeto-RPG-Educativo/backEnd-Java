package com.game.rpgbackend.dto.request.character;

import java.time.LocalDateTime;

/**
 * DTO de requisição para atualizar dados de um personagem.
 * <p>
 * Permite atualizar múltiplos atributos de um personagem existente.
 * Todos os campos são opcionais; apenas os fornecidos serão atualizados.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
public class UpdateCharacterRequest {

    /** Nome do personagem */
    private String name;

    /** Pontos de experiência acumulados */
    private Integer xp;

    /** Quantidade de ouro na carteira */
    private Integer gold;

    /** Pontos de vida atuais */
    private Integer hp;

    /** Energia atual disponível */
    private Integer energy;

    /** Energia máxima do personagem */
    private Integer maxEnergy;

    /** Data e hora do último salvamento */
    private LocalDateTime lastSavedAt;

    /**
     * Construtor padrão.
     */
    public UpdateCharacterRequest() {}

    /** @return nome do personagem */
    public String getName() {
        return name;
    }

    /** @param name nome do personagem */
    public void setName(String name) {
        this.name = name;
    }

    /** @return pontos de experiência */
    public Integer getXp() {
        return xp;
    }

    /** @param xp pontos de experiência */
    public void setXp(Integer xp) {
        this.xp = xp;
    }

    /** @return quantidade de ouro */
    public Integer getGold() {
        return gold;
    }

    /** @param gold quantidade de ouro */
    public void setGold(Integer gold) {
        this.gold = gold;
    }

    /** @return pontos de vida */
    public Integer getHp() {
        return hp;
    }

    /** @param hp pontos de vida */
    public void setHp(Integer hp) {
        this.hp = hp;
    }

    /** @return energia atual */
    public Integer getEnergy() {
        return energy;
    }

    /** @param energy energia atual */
    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    /** @return energia máxima */
    public Integer getMaxEnergy() {
        return maxEnergy;
    }

    /** @param maxEnergy energia máxima */
    public void setMaxEnergy(Integer maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    /** @return data do último salvamento */
    public LocalDateTime getLastSavedAt() {
        return lastSavedAt;
    }

    /** @param lastSavedAt data do último salvamento */
    public void setLastSavedAt(LocalDateTime lastSavedAt) {
        this.lastSavedAt = lastSavedAt;
    }
}
