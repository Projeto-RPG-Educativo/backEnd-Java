package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.NPC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repositório para operações de persistência de NPCs (Non-Player Characters).
 * <p>
 * Gerencia o acesso aos personagens não-jogáveis do Palco da Retórica,
 * permitindo buscar por nome, tipo e localização.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface NPCRepository extends JpaRepository<NPC, Integer> {

    /**
     * Busca um NPC pelo nome único.
     *
     * @param name nome do NPC
     * @return Optional contendo o NPC se encontrado
     */
    Optional<NPC> findByName(String name);

    /**
     * Busca todos os NPCs de um tipo específico.
     *
     * @param type tipo do NPC (mercador, mentor, guarda, etc.)
     * @return lista de NPCs do tipo especificado
     */
    List<NPC> findByType(String type);

    /**
     * Busca todos os NPCs presentes em uma localização.
     *
     * @param location localização no jogo
     * @return lista de NPCs na localização
     */
    List<NPC> findByLocation(String location);
}
