package com.game.rpgbackend.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO de resposta para informações básicas do usuário.
 * <p>
 * Retorna os dados essenciais de um usuário sem informações sensíveis
 * como senha, evitando também referências circulares.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    /**
     * ID único do usuário.
     */
    private Integer id;

    /**
     * Nome de usuário para exibição.
     */
    private String username;

    /**
     * Email do usuário.
     */
    private String email;

    /**
     * Data e hora de criação da conta.
     */
    private LocalDateTime createdAt;
}
