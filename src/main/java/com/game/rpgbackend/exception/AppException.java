package com.game.rpgbackend.exception;

/**
 * Classe base de exceção personalizada para o RPG Backend.
 * Todas as exceções do sistema devem estender esta classe.
 */
public class AppException extends RuntimeException {

    private final int statusCode;

    public AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

