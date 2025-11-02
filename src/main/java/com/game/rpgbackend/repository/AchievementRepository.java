package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência de conquistas (achievements).
 * <p>
 * Gerencia o acesso às conquistas desbloqueadas pelos jogadores,
 * registrando marcos e objetivos especiais alcançados durante o jogo.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Integer> {

    /**
     * Busca todas as conquistas desbloqueadas por um usuário específico.
     *
     * @param userId identificador do usuário
     * @return lista de conquistas do usuário
     */
    List<Achievement> findByUserId(Integer userId);
}
