package com.game.rpgbackend.controller.tutorial;

import com.game.rpgbackend.domain.Dialogue;
import com.game.rpgbackend.domain.NPC;
import com.game.rpgbackend.dto.response.tutorial.DialogueResponseDto;
import com.game.rpgbackend.dto.response.tutorial.NpcResponseDto;
import com.game.rpgbackend.service.tutorial.TutorialDialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operações relacionadas a TutorialDialog.
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/tutorial-dialog")
public class TutorialDialogController {

    @Autowired
    private TutorialDialogService service;

    /**
     * Busca um TutorialDialog pelo ID.
     *
     * @param id o ID do TutorialDialog
     * @return ResponseEntity contendo o TutorialDialog
     */
    @GetMapping("/{id}")
    public ResponseEntity<DialogueResponseDto> getById(@PathVariable Integer id) {
        Dialogue tutorialDialog = service.findById(id);
        if (tutorialDialog == null) {
            return ResponseEntity.notFound().build();
        }

        NPC npc = tutorialDialog.getNpc();
        NpcResponseDto npcDto = null;
        if (npc != null) {
            // converte Integer -> Long se necessário, com checagem nula
            Long npcId = npc.getId() != null ? npc.getId().longValue() : null;
            npcDto = new NpcResponseDto(
                    npcId,
                    npc.getName(),
                    npc.getDescription(),
                    npc.getType(),
                    npc.getLocation()
            );
        }

        // converte id do dialogue também para Long para casar com o DTO
        Long dialogueId = tutorialDialog.getId() != null ? tutorialDialog.getId().longValue() : null;
        DialogueResponseDto dto = new DialogueResponseDto(
                dialogueId,
                tutorialDialog.getContent(),
                tutorialDialog.getResponse(),
                npcDto
        );

        return ResponseEntity.ok(dto);
    }
}
