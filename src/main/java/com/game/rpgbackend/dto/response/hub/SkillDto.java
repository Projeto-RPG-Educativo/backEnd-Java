package com.game.rpgbackend.dto.response.hub;

public class SkillDto {
    private Integer id;
    private String name;
    private String description;
    private Integer cost;
    private String type;
    private String effect;

    public SkillDto() {}

    public SkillDto(Integer id, String name, String description, Integer cost, String type, String effect) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.type = type;
        this.effect = effect;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
}
