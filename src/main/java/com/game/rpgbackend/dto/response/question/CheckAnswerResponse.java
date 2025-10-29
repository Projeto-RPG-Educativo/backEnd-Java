package com.game.rpgbackend.dto.response.question;

public class CheckAnswerResponse {
    private Boolean correct;

    public CheckAnswerResponse() {}

    public CheckAnswerResponse(Boolean correct) {
        this.correct = correct;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
