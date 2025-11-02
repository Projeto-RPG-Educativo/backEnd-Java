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
 * Controller REST responsável pelas operações de salvamento do jogo.
 * <p>
 * Gerencia os slots de save onde o jogador pode salvar e carregar
 * o progresso de seus personagens. Cada save contém o estado completo
 * do personagem (HP, XP, ouro, inventário, etc.) e informações de progresso.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/saves")
@RequiredArgsConstructor
public class SaveController {

    private final SaveService saveService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * Cria um novo save ou atualiza um save existente.
     * <p>
     * Se já existir um save no slot especificado para o usuário,
     * ele será atualizado. Caso contrário, um novo save é criado.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param request dados do save (ID do personagem, slot e estado atual)
     * @return DTO do save criado/atualizado com status 201 Created
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
     * Retorna todos os saves do usuário autenticado.
     * <p>
     * Lista simplificada com informações essenciais de cada save:
     * slot, data, personagem associado.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @return lista com todos os saves do usuário
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
     * Busca um save específico pelo nome do slot.
     * <p>
     * Retorna o save completo com todo o estado do personagem salvo.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param slotName nome do slot de save (ex: "slot1", "slot2")
     * @return DTO completo do save ou 404 se não encontrado
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
     * Remove um save do sistema.
     * <p>
     * Libera o slot para ser usado novamente.
     * </p>
     *
     * @param saveId identificador único do save a ser deletado
     * @return resposta vazia com status 204 No Content
     */
    @DeleteMapping("/{saveId}")
    public ResponseEntity<Void> deleteSave(@PathVariable Integer saveId) {
        saveService.deleteSave(saveId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Converte entidade GameSave para DTO de resposta.
     *
     * @param save entidade de save
     * @return DTO formatado para envio ao cliente
     */
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

    /**
     * Converte resposta do serviço de save para DTO simplificado.
     *
     * @param response objeto de resposta do serviço
     * @return DTO formatado para listagem de saves
     */
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
