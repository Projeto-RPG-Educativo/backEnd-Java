package com.game.rpgbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global para tratamento centralizado de exceções da aplicação.
 * <p>
 * Intercepta exceções lançadas pelos controllers e as converte em
 * respostas HTTP padronizadas com informações de erro estruturadas.
 * Garante consistência no formato de erros retornados pela API.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de requisição inválida (HTTP 400).
     *
     * @param ex exceção de requisição inválida
     * @return resposta de erro formatada com status 400
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata exceções de recurso não encontrado (HTTP 404).
     *
     * @param ex exceção de recurso não encontrado
     * @return resposta de erro formatada com status 404
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Trata exceções de não autorizado (HTTP 401).
     *
     * @param ex exceção de autenticação/autorização
     * @return resposta de erro formatada com status 401
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Trata exceções genéricas não capturadas pelos outros handlers (HTTP 500).
     * <p>
     * Este é o handler de fallback para qualquer exceção não tratada especificamente.
     * </p>
     *
     * @param ex exceção genérica
     * @return resposta de erro formatada com status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse("Erro interno do servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Constrói uma resposta de erro padronizada.
     * <p>
     * Formato inclui timestamp, código de status, nome do erro e mensagem descritiva.
     * </p>
     *
     * @param message mensagem de erro
     * @param status código de status HTTP
     * @return ResponseEntity com mapa de erro estruturado
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);

        return ResponseEntity.status(status).body(errorResponse);
    }
}
