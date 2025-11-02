package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência de diálogos educacionais multilíngues.
 * <p>
 * Gerencia o acesso aos diálogos em português/inglês usados para ensino de idiomas,
 * permitindo filtrar por conteúdo educacional e nível mínimo do personagem.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface DialogRepository extends JpaRepository<Dialog, Integer> {

    /**
     * Busca todos os diálogos de um conteúdo educacional específico.
     *
     * @param contentId identificador do conteúdo
     * @return lista de diálogos do conteúdo
     */
    List<Dialog> findByContentId(Integer contentId);

    /**
     * Busca diálogos acessíveis para um nível específico.
     *
     * @param level nível do personagem
     * @return lista de diálogos disponíveis
     */
    List<Dialog> findByMinLevelLessThanEqual(Integer level);

    /**
     * Busca diálogos de um conteúdo acessíveis para um nível específico.
     *
     * @param contentId identificador do conteúdo
     * @param level nível do personagem
     * @return lista de diálogos filtrados
     */
    List<Dialog> findByContentIdAndMinLevelLessThanEqual(Integer contentId, Integer level);
}
