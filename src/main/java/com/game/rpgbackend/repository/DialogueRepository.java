package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Dialogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência de diálogos de NPCs.
 * <p>
 * Gerencia as falas narrativas dos personagens não-jogáveis no Palco da Retórica,
 * diferentes dos diálogos educacionais multilíngues.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface DialogueRepository extends JpaRepository<Dialogue, Integer> {

    /**
     * Busca todos os diálogos de um NPC específico.
     *
     * @param npcId identificador do NPC
     * @return lista de diálogos do NPC
     */
    List<Dialogue> findByNpcId(Integer npcId);
}
