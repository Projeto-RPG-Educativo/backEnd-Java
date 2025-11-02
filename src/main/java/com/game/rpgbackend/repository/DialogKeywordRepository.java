package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.DialogKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência de palavras-chave de diálogos educacionais.
 * <p>
 * Gerencia as palavras importantes destacadas nos diálogos multilíngues,
 * incluindo suas traduções e configurações de destaque visual.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface DialogKeywordRepository extends JpaRepository<DialogKeyword, Integer> {

    /**
     * Busca todas as palavras-chave de um diálogo específico.
     *
     * @param dialogId identificador do diálogo
     * @return lista de palavras-chave do diálogo
     */
    List<DialogKeyword> findByDialogId(Integer dialogId);

    /**
     * Busca palavras-chave por status de destaque.
     *
     * @param highlight true para palavras destacadas, false para não destacadas
     * @return lista de palavras-chave filtradas
     */
    List<DialogKeyword> findByHighlight(Boolean highlight);
}
