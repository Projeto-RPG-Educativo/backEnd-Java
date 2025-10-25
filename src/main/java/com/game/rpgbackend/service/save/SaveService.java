package com.game.rpgbackend.service.save;

import com.game.rpgbackend.domain.Character;
import com.game.rpgbackend.domain.GameSave;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.CharacterRepository;
import com.game.rpgbackend.repository.GameSaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável pela gestão de saves do jogo.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SaveService {

    private final GameSaveRepository gameSaveRepository;
    private final CharacterRepository characterRepository;

    /**
     * Cria um novo save ou atualiza um existente com o mesmo nome de slot.
     */
    @Transactional
    public GameSave createOrUpdateSave(Integer userId, Integer characterId, String slotName, String currentState) {
        // Verifica se o personagem existe e pertence ao usuário
        Character character = characterRepository.findById(characterId)
            .orElseThrow(() -> new NotFoundException("Personagem não encontrado"));

        if (!character.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Personagem não pertence a este usuário.");
        }

        // Busca ou cria o save
        GameSave save = gameSaveRepository.findByUserIdAndSlotName(userId, slotName)
            .map(existingSave -> {
                existingSave.setCharacterState(currentState);
                existingSave.setSavedAt(LocalDateTime.now());
                return existingSave;
            })
            .orElseGet(() -> {
                GameSave newSave = new GameSave();
                newSave.setUser(character.getUser());
                newSave.setCharacter(character);
                newSave.setSlotName(slotName);
                newSave.setCharacterState(currentState);
                newSave.setSavedAt(LocalDateTime.now());
                return newSave;
            });

        return gameSaveRepository.save(save);
    }

    /**
     * Busca todos os saves de um determinado usuário.
     */
    public List<SaveResponse> getSavesForUser(Integer userId) {
        List<GameSave> saves = gameSaveRepository.findByUserId(userId);

        return saves.stream()
            .map(save -> new SaveResponse(
                save.getId(),
                save.getSlotName(),
                save.getSavedAt(),
                save.getCharacter().getId(),
                save.getCharacter().getName(),
                save.getCharacter().getGameClass().getName()
            ))
            .toList();
    }

    /**
     * Busca um save específico por usuário e slot.
     */
    public GameSave getSaveByUserAndSlot(Integer userId, String slotName) {
        return gameSaveRepository.findByUserIdAndSlotName(userId, slotName)
            .orElseThrow(() -> new NotFoundException("Save não encontrado"));
    }

    /**
     * Deleta um save.
     */
    @Transactional
    public void deleteSave(Integer saveId) {
        if (!gameSaveRepository.existsById(saveId)) {
            throw new NotFoundException("Save não encontrado");
        }
        gameSaveRepository.deleteById(saveId);
    }

    // Classe interna para resposta de save
    public static class SaveResponse {
        private Integer id;
        private String slotName;
        private LocalDateTime savedAt;
        private Integer characterId;
        private String characterName;
        private String characterClass;

        public SaveResponse(Integer id, String slotName, LocalDateTime savedAt, Integer characterId, String characterName, String characterClass) {
            this.id = id;
            this.slotName = slotName;
            this.savedAt = savedAt;
            this.characterId = characterId;
            this.characterName = characterName;
            this.characterClass = characterClass;
        }

        // Getters
        public Integer getId() { return id; }
        public String getSlotName() { return slotName; }
        public LocalDateTime getSavedAt() { return savedAt; }
        public Integer getCharacterId() { return characterId; }
        public String getCharacterName() { return characterName; }
        public String getCharacterClass() { return characterClass; }
    }
}

