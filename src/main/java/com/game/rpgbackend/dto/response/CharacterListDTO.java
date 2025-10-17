package com.game.rpgbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO simplificado para listar personagens (menos informações).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterListDTO {
    private Integer id;
    private String nome;
    private Integer xp;
    private Integer gold;
    private Integer hp;
    private String className;
}

