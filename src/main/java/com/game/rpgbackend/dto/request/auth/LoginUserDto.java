package com.game.rpgbackend.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO de requisição para login de usuário.
 * <p>
 * Esta classe representa os dados necessários para autenticação
 * de um usuário no sistema.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
public class LoginUserDto {

    /**
     * Nome de usuário para autenticação.
     */
    @NotBlank(message = "O nome de usuário é obrigatório")
    private String username;

    /**
     * Senha do usuário para autenticação.
     */
    @NotBlank(message = "A senha é obrigatória")
    private String password;
}
