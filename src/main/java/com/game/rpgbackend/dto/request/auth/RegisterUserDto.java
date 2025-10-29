package com.game.rpgbackend.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de requisição para registro de novo usuário.
 * <p>
 * Contém as informações necessárias para criar uma nova conta de usuário
 * no sistema, incluindo validações de formato e tamanho.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
public class RegisterUserDto {

    /**
     * Nome de usuário único para identificação no sistema.
     * Deve ter entre 3 e 50 caracteres.
     */
    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres")
    private String username;

    /**
     * Endereço de email válido do usuário.
     * Utilizado para comunicação e recuperação de conta.
     */
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    /**
     * Senha do usuário para autenticação.
     * Deve ter no mínimo 6 caracteres.
     */
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;
}
