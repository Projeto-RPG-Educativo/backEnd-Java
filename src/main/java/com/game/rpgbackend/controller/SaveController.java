package com.game.rpgbackend.controller;

import com.game.rpgbackend.domain.GameSave;
import com.game.rpgbackend.service.save.SaveService;
import com.game.rpgbackend.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller responsável pelas operações de save do jogo.
 */
@RestController
@RequestMapping("/api/saves")
@RequiredArgsConstructor
public class SaveController {

    private final SaveService saveService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * Cria ou atualiza um save.
     */
    @PostMapping
    public ResponseEntity<GameSave> createOrUpdateSave(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Object> request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        Integer characterId = (Integer) request.get("characterId");
        String slotName = (String) request.get("slotName");
        String currentState = (String) request.get("currentState");

        GameSave save = saveService.createOrUpdateSave(userId, characterId, slotName, currentState);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    /**
     * Busca todos os saves de um usuário.
     */
    @GetMapping
    public ResponseEntity<List<SaveService.SaveResponse>> getUserSaves(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        List<SaveService.SaveResponse> saves = saveService.getSavesForUser(userId);
        return ResponseEntity.ok(saves);
    }

    /**
     * Busca um save específico por slot.
     */
    @GetMapping("/slot/{slotName}")
    public ResponseEntity<GameSave> getSaveBySlot(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String slotName) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        GameSave save = saveService.getSaveByUserAndSlot(userId, slotName);
        return ResponseEntity.ok(save);
    }

    /**
     * Deleta um save.
     */
    @DeleteMapping("/{saveId}")
    public ResponseEntity<Void> deleteSave(@PathVariable Integer saveId) {
        saveService.deleteSave(saveId);
        return ResponseEntity.noContent().build();
    }
}
