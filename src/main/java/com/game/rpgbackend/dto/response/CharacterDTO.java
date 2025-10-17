package com.game.rpgbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para retornar informações do personagem sem referências circulares.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterDTO {
    private Integer id;
    private String nome;
    private Integer xp;
    private Integer gold;
    private Integer hp;
    private Integer energy;
    private Integer maxEnergy;
    private LocalDateTime lastSavedAt;
    private Integer userId;
    private String userName;
    private Integer classId;
    private String className;
}

