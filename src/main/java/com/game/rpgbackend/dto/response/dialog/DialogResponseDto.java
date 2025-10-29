package com.game.rpgbackend.dto.response.dialog;

import java.util.List;

public class DialogResponseDto {
    private Integer id;
    private String portugues;
    private String ingles;
    private List<KeywordResponseDto> keywords;

    public DialogResponseDto() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPortugues() {
        return portugues;
    }

    public void setPortugues(String portugues) {
        this.portugues = portugues;
    }

    public String getIngles() {
        return ingles;
    }

    public void setIngles(String ingles) {
        this.ingles = ingles;
    }

    public List<KeywordResponseDto> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordResponseDto> keywords) {
        this.keywords = keywords;
    }
}
