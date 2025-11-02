package com.game.rpgbackend.controller.character;

import com.game.rpgbackend.domain.Character;
import com.game.rpgbackend.dto.request.character.CreateCharacterRequest;
import com.game.rpgbackend.dto.request.character.SaveProgressRequest;
import com.game.rpgbackend.dto.request.character.UpdateCharacterRequest;
import com.game.rpgbackend.dto.response.character.CharacterDTO;
import com.game.rpgbackend.dto.response.character.CharacterListDTO;
import com.game.rpgbackend.service.character.CharacterService;
import com.game.rpgbackend.util.AuthenticationUtil;
import com.game.rpgbackend.util.CharacterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST responsável pelas operações de personagens.
 * <p>
 * Gerencia a criação, consulta, atualização e exclusão de personagens,
 * além de operações específicas como salvamento de progresso.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;
    private final AuthenticationUtil authenticationUtil;
    private final CharacterMapper characterMapper;

    /**
     * Busca um personagem específico por ID.
     *
     * @param id identificador único do personagem
     * @return DTO completo do personagem ou 404 se não encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<CharacterDTO> getCharacter(@PathVariable Integer id) {
        return characterService.findCharacterById(id)
                .map(characterMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca todos os personagens do usuário autenticado.
     *
     * @param userDetails detalhes do usuário autenticado
     * @return lista simplificada com todos os personagens do usuário
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
     * Cria um novo personagem para o usuário autenticado.
     * <p>
     * O personagem é inicializado com atributos base da classe escolhida.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param request mapa contendo a classe do personagem (campo "classe")
     * @return DTO do personagem criado com status 201 Created
     */
    @PostMapping
    public ResponseEntity<CharacterDTO> createCharacter(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateCharacterRequest request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        String className = request.getClasse();

        if (className == null || className.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Character newCharacter = characterService.createCharacter(userId, className);
        return ResponseEntity.status(HttpStatus.CREATED).body(characterMapper.toDTO(newCharacter));
    }

    /**
     * Cria um novo personagem para o tutorial, fixando a classe como Lutador (Guerreiro).
     * <p>
     * Este endpoint é específico para o fluxo de tutorial onde a classe
     * é pré-determinada como Lutador para garantir uma experiência consistente
     * de aprendizado para novos jogadores.
     * </p>
     * <p>
     * O personagem é inicializado com atributos base da classe Lutador:
     * - HP, energia e gold iniciais
     * - Atributos de força e defesa balanceados
     * - Habilidade especial "Investida"
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @return DTO do personagem criado com status 201 Created
     */
    @PostMapping("/tutorial")
    public ResponseEntity<CharacterDTO> createTutorialCharacter(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        // Fixa a classe como Lutador para o tutorial
        String className = "lutador";

        Character newCharacter = characterService.createCharacter(userId, className);
        return ResponseEntity.status(HttpStatus.CREATED).body(characterMapper.toDTO(newCharacter));
    }

    /**
     * Salva o progresso do personagem (XP e HP).
     * <p>
     * Atualiza os valores de experiência e pontos de vida do personagem.
     * </p>
     *
     * @param request mapa contendo characterId, xp e hp
     * @return DTO do personagem atualizado
     */
    @PutMapping("/progress")
    public ResponseEntity<CharacterDTO> saveProgress(@Valid @RequestBody SaveProgressRequest request) {
        Integer characterId = request.getCharacterId();
        Integer xp = request.getXp();
        Integer hp = request.getHp();

        if (characterId == null || xp == null || hp == null) {
            return ResponseEntity.badRequest().build();
        }

        Character updated = characterService.updateCharacterProgress(characterId, xp, hp);
        return ResponseEntity.ok(characterMapper.toDTO(updated));
    }

    /**
     * Atualiza os dados de um personagem.
     *
     * @param id identificador do personagem
     * @param request objeto com os novos dados do personagem
     * @return DTO do personagem atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<CharacterDTO> updateCharacter(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCharacterRequest request) {

        // Mapear request para Character
        Character character = new Character();
        character.setName(request.getName());
        character.setXp(request.getXp());
        character.setGold(request.getGold());
        character.setHp(request.getHp());
        character.setEnergy(request.getEnergy());
        character.setMaxEnergy(request.getMaxEnergy());
        character.setLastSavedAt(request.getLastSavedAt());

        Character updated = characterService.updateCharacter(id, character);
        return ResponseEntity.ok(characterMapper.toDTO(updated));
    }

    /**
     * Remove um personagem do sistema.
     *
     * @param id identificador do personagem a ser removido
     * @return resposta vazia com status 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Integer id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
