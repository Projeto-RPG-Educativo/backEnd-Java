package com.game.rpgbackend.dto.request.question;

/**
 * DTO de requisição para verificar resposta de uma questão.
 * <p>
 * Contém o ID da questão e a resposta fornecida pelo jogador
 * para validação no servidor.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
public class CheckAnswerRequest {

    /** ID da questão sendo respondida */
    private Integer questionId;

    /** Resposta fornecida pelo jogador */
    private String answer;

    /**
     * Construtor padrão.
     */
    public CheckAnswerRequest() {}

    /**
     * Construtor com todos os campos.
     *
     * @param questionId ID da questão
     * @param answer resposta do jogador
     */
    public CheckAnswerRequest(Integer questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    /** @return ID da questão */
    public Integer getQuestionId() {
        return questionId;
    }

    /** @param questionId ID da questão */
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /** @return resposta do jogador */
    public String getAnswer() {
        return answer;
    }

    /** @param answer resposta do jogador */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
