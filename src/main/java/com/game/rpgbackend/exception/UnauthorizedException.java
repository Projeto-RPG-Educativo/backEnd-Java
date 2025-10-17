package com.game.rpgbackend.exception;

/**
 * Exceção lançada quando uma autenticação falha.
 * HTTP Status: 401 Unauthorized
 */
public class UnauthorizedException extends AppException {

    public UnauthorizedException(String message) {
        super(message, 401);
    }
}

