package com.game.rpgbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para login de usuário.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String message;
    private UserDto user;
    private String token;
}

