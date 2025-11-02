package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repositório para operações de persistência de habilidades especiais.
 * <p>
 * Gerencia o acesso às habilidades desbloqueaveis na Torre do Conhecimento,
 * permitindo buscar por nome e tipo de habilidade.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {

    /**
     * Busca uma habilidade pelo nome único.
     *
     * @param name nome da habilidade
     * @return Optional contendo a habilidade se encontrada
     */
    Optional<Skill> findByName(String name);

    /**
     * Busca todas as habilidades de um tipo específico.
     *
     * @param type tipo da habilidade (passiva, ativa, utilitária)
     * @return lista de habilidades do tipo especificado
     */
    List<Skill> findByType(String type);
}
