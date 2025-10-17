package com.game.rpgbackend.exception;

/**
 * Exceção lançada quando uma requisição é inválida.
 * HTTP Status: 400 Bad Request
 */
public class BadRequestException extends AppException {

    public BadRequestException(String message) {
        super(message, 400);
    }
}

