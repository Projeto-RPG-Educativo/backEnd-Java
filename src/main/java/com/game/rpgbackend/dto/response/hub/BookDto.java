package com.game.rpgbackend.dto.response.hub;

public class BookDto {
    private Integer id;
    private String title;
    private String content;
    private String type;
    private String difficulty;

    public BookDto() {}

    public BookDto(Integer id, String title, String content, String type, String difficulty) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.difficulty = difficulty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
