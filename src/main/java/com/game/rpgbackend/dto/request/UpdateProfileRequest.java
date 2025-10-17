package com.game.rpgbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(min = 3, max = 20)
    private String nome;

    @Email
    private String email;

    private String senhaAtual;

    @Size(min = 6)
    private String novaSenha;
}
