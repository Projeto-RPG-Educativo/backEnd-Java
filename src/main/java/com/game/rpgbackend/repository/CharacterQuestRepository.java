package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.CharacterQuest;
import com.game.rpgbackend.domain.CharacterQuestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência do progresso de quests por personagem.
 * <p>
 * Gerencia a relação many-to-many entre personagens e quests,
 * rastreando quais quests cada personagem está realizando e seus status.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface CharacterQuestRepository extends JpaRepository<CharacterQuest, CharacterQuestId> {

    /**
     * Busca todas as quests de um personagem específico.
     *
     * @param characterId identificador do personagem
     * @return lista de quests do personagem
     */
    List<CharacterQuest> findByCharacterId(Integer characterId);

    /**
     * Busca todos os personagens que estão realizando uma quest específica.
     *
     * @param questId identificador da quest
     * @return lista de personagens na quest
     */
    List<CharacterQuest> findByQuestId(Integer questId);

    /**
     * Busca todas as quests com um status específico.
     *
     * @param status status da quest (in_progress, completed, failed)
     * @return lista de quests com o status especificado
     */
    List<CharacterQuest> findByStatus(String status);
}
