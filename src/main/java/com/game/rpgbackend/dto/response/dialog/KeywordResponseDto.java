package com.game.rpgbackend.dto.response.dialog;

public class KeywordResponseDto {
    private String palavra;
    private String traducao;
    private Boolean destaque;

    public KeywordResponseDto() {}

    public KeywordResponseDto(String palavra, String traducao, Boolean destaque) {
        this.palavra = palavra;
        this.traducao = traducao;
        this.destaque = destaque;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String getTraducao() {
        return traducao;
    }

    public void setTraducao(String traducao) {
        this.traducao = traducao;
    }

    public Boolean getDestaque() {
        return destaque;
    }

    public void setDestaque(Boolean destaque) {
        this.destaque = destaque;
    }
}
