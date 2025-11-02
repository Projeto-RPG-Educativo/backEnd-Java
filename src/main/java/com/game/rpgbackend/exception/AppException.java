package com.game.rpgbackend.exception;

/**
 * Classe base de exceção personalizada para o RPG Backend.
 * <p>
 * Todas as exceções customizadas do sistema devem estender esta classe.
 * Armazena o código de status HTTP associado ao erro.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
public class AppException extends RuntimeException {

    private final int statusCode;

    /**
     * Construtor da exceção personalizada.
     *
     * @param message mensagem descritiva do erro
     * @param statusCode código de status HTTP associado
     */
    public AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Retorna o código de status HTTP do erro.
     *
     * @return código de status HTTP
     */
    public int getStatusCode() {
        return statusCode;
    }
}
