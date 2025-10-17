package com.game.rpgbackend.exception;

/**
 * Exceção lançada quando um recurso não é encontrado.
 * HTTP Status: 404 Not Found
 */
public class NotFoundException extends AppException {

    public NotFoundException(String message) {
        super(message, 404);
    }
}

