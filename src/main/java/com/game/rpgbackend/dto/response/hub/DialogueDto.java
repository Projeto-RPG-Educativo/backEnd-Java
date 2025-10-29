package com.game.rpgbackend.dto.response.hub;

public class DialogueDto {
    private Integer id;
    private String content;
    private String response;

    public DialogueDto() {}

    public DialogueDto(Integer id, String content, String response) {
        this.id = id;
        this.content = content;
        this.response = response;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
