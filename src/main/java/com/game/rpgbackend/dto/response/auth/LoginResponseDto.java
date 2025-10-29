package com.game.rpgbackend.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para login de usuário.
 * <p>
 * Retorna as informações de autenticação após um login bem-sucedido,
 * incluindo mensagem de confirmação, dados do usuário e token JWT.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    /**
     * Mensagem de confirmação do login.
     */
    private String message;

    /**
     * Dados do usuário autenticado.
     */
    private UserDto user;

    /**
     * Token JWT para autenticação nas próximas requisições.
     */
    private String token;
}
