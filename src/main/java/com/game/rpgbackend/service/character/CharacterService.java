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
 * Serviço responsável pela gestão de personagens do jogador.
 * <p>
 * Gerencia todo o ciclo de vida dos personagens:
 * - Criação com classe e atributos iniciais
 * - Progressão de nível e experiência
 * - Atualização de atributos (HP, energia, ouro)
 * - Sistema de level up automático
 * - Inicialização de inventário
 * - Exclusão de personagens
 * </p>
 * <p>
 * Cada personagem é vinculado a um usuário e uma classe jogável,
 * herdando os atributos base da classe escolhida.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
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

    /**
     * Busca um personagem por seu identificador único.
     *
     * @param id identificador do personagem
     * @return Optional contendo o personagem se encontrado
     */
    public Optional<Character> findCharacterById(Integer id) {
        return characterRepository.findById(id);
    }

    /**
     * Retorna todos os personagens pertencentes a um usuário.
     *
     * @param userId identificador do usuário
     * @return lista de personagens do usuário
     */
    public List<Character> findByUserId(Integer userId) {
        return characterRepository.findByUserId(userId);
    }

    /**
     * Calcula a quantidade de XP necessária para atingir o próximo nível.
     * <p>
     * A fórmula é progressiva: XP = baseXP * (nível ^ multiplicador)
     * Configurável através do GameConfig.
     * </p>
     *
     * @param currentLevel nível atual do personagem
     * @return quantidade de XP necessária para subir de nível
     */
    private int calculateXpForNextLevel(int currentLevel) {
        int baseXp = gameConfig.getLeveling().getBaseXp();
        double xpMultiplier = gameConfig.getLeveling().getXpMultiplier();
        return (int) Math.floor(baseXp * Math.pow(currentLevel, xpMultiplier));
    }

    /**
     * Atualiza o progresso do personagem após uma batalha ou atividade.
     * <p>
     * Atualiza XP e HP, processando automaticamente level ups quando
     * o XP atinge o threshold necessário.
     * </p>
     *
     * @param characterId identificador do personagem
     * @param xp novo valor de experiência
     * @param hp novo valor de HP
     * @return personagem atualizado com novos valores
     * @throws IllegalArgumentException se algum parâmetro for nulo
     * @throws NotFoundException se o personagem não for encontrado
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
     * Verifica se o personagem possui XP suficiente para subir de nível e aplica o level up.
     * <p>
     * Se o XP atual for maior ou igual ao necessário:
     * - Incrementa o nível nas estatísticas do jogador
     * - Concede pontos de habilidade
     * - Deduz o XP consumido, mantendo o excedente
     * - Retorna mensagem de sucesso
     * </p>
     * <p>
     * O cálculo de XP necessário é baseado em uma fórmula exponencial
     * configurável no GameConfig.
     * </p>
     *
     * @param characterId identificador do personagem
     * @return resultado indicando se houve level up e mensagem correspondente
     * @throws NotFoundException se o personagem ou estatísticas não forem encontradas
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
     * Cria um novo personagem vinculado a um usuário com a classe especificada.
     * <p>
     * Processo de criação:
     * 1. Valida e busca a classe no banco de dados
     * 2. Cria o personagem com atributos base da classe
     * 3. Inicializa HP, energia e ouro
     * 4. Cria um inventário vazio associado
     * 5. Persiste tudo no banco de dados
     * </p>
     * <p>
     * O personagem começa no nível do usuário com HP e energia máximos.
     * </p>
     *
     * @param userId identificador do usuário que criará o personagem
     * @param className nome da classe do personagem (lutador, mago, bardo, etc.)
     * @return personagem criado e persistido com inventário inicializado
     * @throws IllegalArgumentException se a classe não for encontrada
     * @throws NotFoundException se o usuário não for encontrado
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
     * Atualiza os atributos de um personagem existente.
     * <p>
     * Permite atualizar apenas os campos fornecidos (não-nulos):
     * - Nome do personagem
     * - HP atual
     * - XP acumulado
     * - Ouro na carteira
     * </p>
     * <p>
     * Campos não fornecidos mantêm seus valores originais.
     * </p>
     *
     * @param id identificador do personagem a ser atualizado
     * @param characterData objeto com os novos valores (campos nulos são ignorados)
     * @return personagem atualizado e persistido
     * @throws NotFoundException se o personagem não for encontrado
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
     * Remove permanentemente um personagem do sistema.
     * <p>
     * Deleta o personagem e todos os seus dados relacionados
     * (inventário, itens, progresso). Esta operação não pode ser desfeita.
     * </p>
     *
     * @param id identificador do personagem a ser deletado
     * @throws NotFoundException se o personagem não for encontrado
     */
    @Transactional
    public void deleteCharacter(Integer id) {
        if (!characterRepository.existsById(id)) {
            throw new NotFoundException("Personagem não encontrado");
        }
        characterRepository.deleteById(id);
    }

    /**
     * Classe interna para representar o resultado de uma verificação de level up.
     * <p>
     * Encapsula se houve aumento de nível e uma mensagem descritiva do resultado.
     * </p>
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
