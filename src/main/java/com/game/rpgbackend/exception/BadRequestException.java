package com.game.rpgbackend.exception;

/**
 * Exceção lançada quando uma requisição é inválida ou malformada.
 * <p>
 * Utilizada para indicar erros de validação de dados ou parâmetros incorretos.
 * Resulta em resposta HTTP 400 Bad Request.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
public class BadRequestException extends AppException {

    /**
     * Construtor da exceção de requisição inválida.
     *
     * @param message descrição do erro de validação
     */
    public BadRequestException(String message) {
        super(message, 400);
    }
}
