package com.game.rpgbackend.exception;

/**
 * Exceção lançada quando uma autenticação ou autorização falha.
 * <p>
 * Utilizada para indicar credenciais inválidas, tokens expirados ou
 * tentativas de acesso não autorizado. Resulta em resposta HTTP 401 Unauthorized.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
public class UnauthorizedException extends AppException {

    /**
     * Construtor da exceção de não autorizado.
     *
     * @param message descrição do erro de autenticação/autorização
     */
    public UnauthorizedException(String message) {
        super(message, 401);
    }
}
