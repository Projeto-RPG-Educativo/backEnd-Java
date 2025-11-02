package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.QuestRewardItem;
import com.game.rpgbackend.domain.QuestRewardItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência de recompensas de quests.
 * <p>
 * Gerencia a relação many-to-many entre quests e itens dados como recompensa,
 * incluindo as quantidades de cada item por quest completada.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface QuestRewardItemRepository extends JpaRepository<QuestRewardItem, QuestRewardItemId> {

    /**
     * Busca todos os itens dados como recompensa de uma quest específica.
     *
     * @param questId identificador da quest
     * @return lista de itens recompensa da quest
     */
    List<QuestRewardItem> findByQuestId(Integer questId);

    /**
     * Busca todas as quests que dão um item específico como recompensa.
     *
     * @param itemId identificador do item
     * @return lista de quests que recompensam o item
     */
    List<QuestRewardItem> findByItemId(Integer itemId);
}
