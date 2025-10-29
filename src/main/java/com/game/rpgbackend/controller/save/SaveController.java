package com.game.rpgbackend.controller.save;

import com.game.rpgbackend.domain.GameSave;
import com.game.rpgbackend.service.save.SaveService;
import com.game.rpgbackend.util.AuthenticationUtil;
import com.game.rpgbackend.dto.request.save.SaveRequestDto;
import com.game.rpgbackend.dto.response.save.GameSaveDto;
import com.game.rpgbackend.dto.response.save.SaveResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<GameSaveDto> createOrUpdateSave(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SaveRequestDto request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        Integer characterId = request.getCharacterId() != null ? request.getCharacterId().intValue() : null;
        String slotName = request.getSlotName();

        // Converte JsonNode para String para persistir
        String currentState;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = request.getCurrentState();
            currentState = mapper.writeValueAsString(node);
        } catch (Exception e) {
            currentState = "{}";
        }

        GameSave save = saveService.createOrUpdateSave(userId, characterId, slotName, currentState);
        GameSaveDto dto = mapToDto(save);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Busca todos os saves de um usuário.
     */
    @GetMapping
    public ResponseEntity<List<SaveResponseDto>> getUserSaves(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        List<SaveService.SaveResponse> saves = saveService.getSavesForUser(userId);
        List<SaveResponseDto> dtoList = saves.stream().map(this::mapSaveResponseToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Busca um save específico por slot.
     */
    @GetMapping("/slot/{slotName}")
    public ResponseEntity<GameSaveDto> getSaveBySlot(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String slotName) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        GameSave save = saveService.getSaveByUserAndSlot(userId, slotName);
        GameSaveDto dto = mapToDto(save);
        return ResponseEntity.ok(dto);
    }

    /**
     * Deleta um save.
     */
    @DeleteMapping("/{saveId}")
    public ResponseEntity<Void> deleteSave(@PathVariable Integer saveId) {
        saveService.deleteSave(saveId);
        return ResponseEntity.noContent().build();
    }

    private GameSaveDto mapToDto(GameSave save) {
        GameSaveDto dto = new GameSaveDto();
        dto.setId(save.getId());
        dto.setSlotName(save.getSlotName());
        dto.setSavedAt(save.getSavedAt());
        dto.setCharacterState(save.getCharacterState());
        dto.setUserId(save.getUser().getId());
        dto.setCharacterId(save.getCharacter().getId());
        return dto;
    }

    private SaveResponseDto mapSaveResponseToDto(SaveService.SaveResponse response) {
        SaveResponseDto dto = new SaveResponseDto(
            response.getId(),
            response.getSlotName(),
            response.getSavedAt(),
            response.getCharacterId(),
            response.getCharacterName(),
            response.getCharacterClass()
        );
        return dto;
    }
}
