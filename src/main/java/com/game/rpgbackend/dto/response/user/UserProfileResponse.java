package com.game.rpgbackend.dto.response.user;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de resposta para perfil completo do usuário.
 * <p>
 * Contém informações detalhadas do perfil do usuário,
 * incluindo estatísticas básicas como total de personagens.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
public class UserProfileResponse {
    /** ID único do usuário */
    private Integer id;

    /** Nome do usuário */
    private String nome;

    /** Email do usuário */
    private String email;

    /** Data de criação da conta */
    private LocalDateTime dataCriacao;

    /** Quantidade total de personagens criados */
    private Integer totalPersonagens;
}
