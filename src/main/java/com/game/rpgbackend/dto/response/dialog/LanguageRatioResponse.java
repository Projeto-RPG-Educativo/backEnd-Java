package com.game.rpgbackend.dto.response.dialog;

public class LanguageRatioResponse {
    private Double ratio;

    public LanguageRatioResponse() {}

    public LanguageRatioResponse(Double ratio) {
        this.ratio = ratio;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }
}
