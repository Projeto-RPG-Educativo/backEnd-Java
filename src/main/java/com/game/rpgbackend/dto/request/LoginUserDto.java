package com.game.rpgbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO de requisição para login de usuário.
 */
@Data
public class LoginUserDto {

    @NotBlank(message = "O nome de usuário é obrigatório")
    private String nomeUsuario;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;
}

