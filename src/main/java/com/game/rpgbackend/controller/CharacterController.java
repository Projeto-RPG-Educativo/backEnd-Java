package com.game.rpgbackend.controller;

import com.game.rpgbackend.domain.Character;
import com.game.rpgbackend.dto.response.CharacterDTO;
import com.game.rpgbackend.dto.response.CharacterListDTO;
import com.game.rpgbackend.service.character.CharacterService;
import com.game.rpgbackend.util.AuthenticationUtil;
import com.game.rpgbackend.util.CharacterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller responsável pelas operações de personagens.
 */
@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;
    private final AuthenticationUtil authenticationUtil;
    private final CharacterMapper characterMapper;

    /**
     * Busca um personagem por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CharacterDTO> getCharacter(@PathVariable Integer id) {
        return characterService.findCharacterById(id)
                .map(characterMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca todos os personagens de um usuário.
     */
    @GetMapping("/user")
    public ResponseEntity<List<CharacterListDTO>> getUserCharacters(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        List<CharacterListDTO> characters = characterService.findByUserId(userId)
                .stream()
                .map(characterMapper::toListDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(characters);
    }

    /**
     * Cria um novo personagem.
     */
    @PostMapping
    public ResponseEntity<CharacterDTO> createCharacter(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        String className = request.get("classe");

        if (className == null || className.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Character newCharacter = characterService.createCharacter(userId, className);
        return ResponseEntity.status(HttpStatus.CREATED).body(characterMapper.toDTO(newCharacter));
    }

    /**
     * Salva o progresso do personagem (XP e HP).
     */
    @PutMapping("/progress")
    public ResponseEntity<CharacterDTO> saveProgress(@RequestBody Map<String, Integer> request) {
        Integer characterId = request.get("characterId");
        Integer xp = request.get("xp");
        Integer hp = request.get("hp");

        if (characterId == null || xp == null || hp == null) {
            return ResponseEntity.badRequest().build();
        }

        Character updated = characterService.updateCharacterProgress(characterId, xp, hp);
        return ResponseEntity.ok(characterMapper.toDTO(updated));
    }

    /**
     * Atualiza um personagem.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CharacterDTO> updateCharacter(
            @PathVariable Integer id,
            @RequestBody Character character) {

        Character updated = characterService.updateCharacter(id, character);
        return ResponseEntity.ok(characterMapper.toDTO(updated));
    }

    /**
     * Deleta um personagem.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Integer id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
