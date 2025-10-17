package com.game.rpgbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para retornar informações do usuário sem referências circulares.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String nomeUsuario;
    private String email;
    private LocalDateTime criadoEm;
}

