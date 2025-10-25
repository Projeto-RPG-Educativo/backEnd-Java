package com.game.rpgbackend.service.character;

import com.game.rpgbackend.config.GameConfig;
import com.game.rpgbackend.domain.Character;
import com.game.rpgbackend.domain.GameClass;
import com.game.rpgbackend.domain.Inventory;
import com.game.rpgbackend.domain.PlayerStats;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.CharacterRepository;
import com.game.rpgbackend.repository.ClassRepository;
import com.game.rpgbackend.repository.InventoryRepository;
import com.game.rpgbackend.repository.PlayerStatsRepository;
import com.game.rpgbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela gestão de personagens.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final ClassRepository classRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final GameConfig gameConfig;

    public Optional<Character> findCharacterById(Integer id) {
        return characterRepository.findById(id);
    }

    public List<Character> findByUserId(Integer userId) {
        return characterRepository.findByUserId(userId);
    }

    /**
     * Calcula o XP necessário para o próximo nível.
     */
    private int calculateXpForNextLevel(int currentLevel) {
        int baseXp = gameConfig.getLeveling().getBaseXp();
        double xpMultiplier = gameConfig.getLeveling().getXpMultiplier();
        return (int) Math.floor(baseXp * Math.pow(currentLevel, xpMultiplier));
    }

    /**
     * Atualiza o progresso do personagem (XP e HP).
     */
    @Transactional
    public Character updateCharacterProgress(Integer characterId, Integer xp, Integer hp) {
        if (characterId == null || xp == null || hp == null) {
            throw new IllegalArgumentException("Dados insuficientes para salvar o progresso.");
        }

        Character character = characterRepository.findById(characterId)
            .orElseThrow(() -> new NotFoundException("Personagem não encontrado"));

        character.setXp(xp);
        character.setHp(hp);

        return characterRepository.save(character);
    }

    /**
     * Verifica se um personagem tem XP suficiente para subir de nível e aplica o level up.
     */
    @Transactional
    public LevelUpResult checkForLevelUp(Integer characterId) {
        Character character = characterRepository.findById(characterId)
            .orElseThrow(() -> new NotFoundException("Personagem não encontrado"));

        if (character.getUser().getStats() == null) {
            throw new NotFoundException("Estatísticas do usuário não encontradas.");
        }

        PlayerStats stats = character.getUser().getStats();
        int currentLevel = stats.getLevel();
        int currentXp = character.getXp();
        int xpNeeded = calculateXpForNextLevel(currentLevel);

        if (currentXp >= xpNeeded) {
            // LEVEL UP!
            int newLevel = currentLevel + 1;
            int remainingXp = currentXp - xpNeeded;
            int skillPointsGained = 1;

            // Atualiza estatísticas do jogador
            stats.setLevel(newLevel);
            stats.setSkillPoints(stats.getSkillPoints() + skillPointsGained);
            playerStatsRepository.save(stats);

            // Atualiza XP do personagem
            character.setXp(remainingXp);
            characterRepository.save(character);

            return new LevelUpResult(true, String.format("Parabéns! Você alcançou o nível %d!", newLevel));
        }

        return new LevelUpResult(false, "Você ganhou XP!");
    }

    /**
     * Cria um novo personagem para um usuário.
     */
    @Transactional
    public Character createCharacter(Integer userId, String className) {
        // 1. Busca a classe
        GameClass characterClass = classRepository.findByName(className)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Classe '%s' não encontrada no banco de dados.", className)
            ));

        // 2. Cria o novo personagem
        Character newCharacter = new Character();
        newCharacter.setUser(userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado")));
        newCharacter.setName("Herói " + characterClass.getName());
        newCharacter.setHp(characterClass.getHp());
        newCharacter.setXp(0);
        newCharacter.setGold(0);
        newCharacter.setEnergy(characterClass.getStamina() != null ? characterClass.getStamina() : 10);
        newCharacter.setMaxEnergy(characterClass.getStamina() != null ? characterClass.getStamina() : 10);
        newCharacter.setGameClass(characterClass);

        Character savedCharacter = characterRepository.save(newCharacter);

        // 3. Cria um inventário vazio
        Inventory inventory = new Inventory();
        inventory.setCharacter(savedCharacter);
        inventoryRepository.save(inventory);

        return savedCharacter;
    }

    /**
     * Atualiza um personagem existente.
     */
    @Transactional
    public Character updateCharacter(Integer id, Character characterData) {
        Character existing = characterRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Personagem não encontrado"));

        if (characterData.getName() != null) {
            existing.setName(characterData.getName());
        }
        if (characterData.getHp() != null) {
            existing.setHp(characterData.getHp());
        }
        if (characterData.getXp() != null) {
            existing.setXp(characterData.getXp());
        }
        if (characterData.getGold() != null) {
            existing.setGold(characterData.getGold());
        }

        return characterRepository.save(existing);
    }

    /**
     * Deleta um personagem.
     */
    @Transactional
    public void deleteCharacter(Integer id) {
        if (!characterRepository.existsById(id)) {
            throw new NotFoundException("Personagem não encontrado");
        }
        characterRepository.deleteById(id);
    }

    /**
     * Classe interna para representar o resultado de um level up.
     */
    public static class LevelUpResult {
        private final boolean leveledUp;
        private final String message;

        public LevelUpResult(boolean leveledUp, String message) {
            this.leveledUp = leveledUp;
            this.message = message;
        }

        public boolean isLeveledUp() {
            return leveledUp;
        }

        public String getMessage() {
            return message;
        }
    }
}
