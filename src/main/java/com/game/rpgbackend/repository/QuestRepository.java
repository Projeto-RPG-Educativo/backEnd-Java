package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório para operações de persistência de quests/missões.
 * <p>
 * Gerencia o acesso às missões disponíveis no jogo,
 * incluindo seus objetivos, recompensas e requisitos.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface QuestRepository extends JpaRepository<Quest, Integer> {

    /**
     * Busca uma quest pelo título único.
     *
     * @param title título da quest
     * @return Optional contendo a quest se encontrada
     */
    Optional<Quest> findByTitle(String title);
}
