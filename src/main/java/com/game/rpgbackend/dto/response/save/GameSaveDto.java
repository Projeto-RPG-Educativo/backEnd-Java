package com.game.rpgbackend.dto.response.save;

import java.time.LocalDateTime;

public class GameSaveDto {
    private Integer id;
    private String slotName;
    private LocalDateTime savedAt;
    private String characterState;
    private Integer userId;
    private Integer characterId;

    public GameSaveDto() {}

    // getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }

    public String getCharacterState() {
        return characterState;
    }

    public void setCharacterState(String characterState) {
        this.characterState = characterState;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }
}
