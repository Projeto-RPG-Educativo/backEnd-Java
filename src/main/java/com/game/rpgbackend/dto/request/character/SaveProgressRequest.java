package com.game.rpgbackend.dto.request.character;

public class SaveProgressRequest {
    private Integer characterId;
    private Integer xp;
    private Integer hp;

    public SaveProgressRequest() {}

    public SaveProgressRequest(Integer characterId, Integer xp, Integer hp) {
        this.characterId = characterId;
        this.xp = xp;
        this.hp = hp;
    }

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }
}
