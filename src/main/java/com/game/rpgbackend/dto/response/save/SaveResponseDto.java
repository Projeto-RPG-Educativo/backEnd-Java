package com.game.rpgbackend.dto.response.save;

import java.time.LocalDateTime;

public class SaveResponseDto {
    private Integer id;
    private String slotName;
    private LocalDateTime savedAt;
    private Integer characterId;
    private String characterName;
    private String characterClass;

    public SaveResponseDto() {}

    public SaveResponseDto(Integer id, String slotName, LocalDateTime savedAt, Integer characterId, String characterName, String characterClass) {
        this.id = id;
        this.slotName = slotName;
        this.savedAt = savedAt;
        this.characterId = characterId;
        this.characterName = characterName;
        this.characterClass = characterClass;
    }

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

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }
}
