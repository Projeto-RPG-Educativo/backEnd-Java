package com.game.rpgbackend.dto.request.character;

import java.time.LocalDateTime;

public class UpdateCharacterRequest {
    private String name;
    private Integer xp;
    private Integer gold;
    private Integer hp;
    private Integer energy;
    private Integer maxEnergy;
    private LocalDateTime lastSavedAt;

    public UpdateCharacterRequest() {}

    // getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getEnergy() {
        return energy;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public Integer getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(Integer maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public LocalDateTime getLastSavedAt() {
        return lastSavedAt;
    }

    public void setLastSavedAt(LocalDateTime lastSavedAt) {
        this.lastSavedAt = lastSavedAt;
    }
}
