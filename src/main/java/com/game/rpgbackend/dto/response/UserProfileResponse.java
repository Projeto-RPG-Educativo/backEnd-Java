package com.game.rpgbackend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserProfileResponse {
    private Integer id;
    private String nome;
    private String email;
    private LocalDateTime dataCriacao;
    private Integer totalPersonagens;
}
