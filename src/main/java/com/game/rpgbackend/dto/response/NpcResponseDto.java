package com.game.rpgbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NpcResponseDto {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String location;
}
