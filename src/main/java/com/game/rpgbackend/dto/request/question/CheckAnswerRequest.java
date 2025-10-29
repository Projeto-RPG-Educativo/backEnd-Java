package com.game.rpgbackend.dto.request.question;

public class CheckAnswerRequest {
    private Integer questionId;
    private String answer;

    public CheckAnswerRequest() {}

    public CheckAnswerRequest(Integer questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
