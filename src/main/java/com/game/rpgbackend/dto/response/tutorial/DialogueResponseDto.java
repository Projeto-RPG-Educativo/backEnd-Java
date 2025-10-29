package com.game.rpgbackend.dto.response.tutorial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DialogueResponseDto {
    private Long id;
    private String content;
    private String response;
    private NpcResponseDto npc;
}