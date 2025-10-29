package com.game.rpgbackend.dto.response.hub;

public class NpcDto {
    private Integer id;
    private String name;
    private String description;
    private String type;
    private String location;

    public NpcDto() {}

    public NpcDto(Integer id, String name, String description, String type, String location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.location = location;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
