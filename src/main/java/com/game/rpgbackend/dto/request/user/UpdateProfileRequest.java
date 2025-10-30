package com.game.rpgbackend.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de requisição para atualização de perfil do usuário.
 * <p>
 * Permite ao usuário atualizar suas informações pessoais,
 * incluindo nome, email e senha. Todos os campos são opcionais.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
public class    UpdateProfileRequest {

    /**
     * Novo nome do usuário (opcional).
     * Deve ter entre 3 e 20 caracteres se fornecido.
     */
    @Size(min = 3, max = 20)
    private String nome;

    /**
     * Novo email do usuário (opcional).
     * Deve ser um email válido se fornecido.
     */
    @Email
    private String email;

    /**
     * Senha atual do usuário (obrigatória para mudança de senha).
     */
    private String senhaAtual;

    /**
     * Nova senha desejada (opcional).
     * Deve ter no mínimo 6 caracteres se fornecida.
     */
    @Size(min = 6)
    private String novaSenha;
}
