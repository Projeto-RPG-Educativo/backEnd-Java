package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência de itens do jogo.
 * <p>
 * Gerencia o acesso aos itens disponíveis no sistema, incluindo
 * armas, armaduras, consumíveis e itens de quest.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    /**
     * Busca todos os itens de um tipo específico.
     *
     * @param type tipo do item (arma, armadura, consumível, etc.)
     * @return lista de itens do tipo especificado
     */
    List<Item> findByType(String type);
}
