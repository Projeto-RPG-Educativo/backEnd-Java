package com.game.rpgbackend.exception;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado.
 * <p>
 * Utilizada quando uma busca por ID ou outro identificador único
 * não retorna nenhum resultado. Resulta em resposta HTTP 404 Not Found.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
public class NotFoundException extends AppException {

    /**
     * Construtor da exceção de recurso não encontrado.
     *
     * @param message descrição do recurso que não foi encontrado
     */
    public NotFoundException(String message) {
        super(message, 404);
    }
}
