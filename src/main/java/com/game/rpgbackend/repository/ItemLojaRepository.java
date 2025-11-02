package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.ItemStore;
import com.game.rpgbackend.domain.ItemStoreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência de itens em lojas.
 * <p>
 * Gerencia a relação many-to-many entre lojas e itens disponíveis,
 * incluindo preços e quantidades em estoque de cada loja.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ItemLojaRepository extends JpaRepository<ItemStore, ItemStoreId> {

    /**
     * Busca todos os itens disponíveis em uma loja específica.
     *
     * @param storeId identificador da loja
     * @return lista de itens vendidos na loja
     */
    List<ItemStore> findByStoreId(Integer storeId);

    /**
     * Busca todas as lojas que vendem um item específico.
     *
     * @param itemId identificador do item
     * @return lista de lojas que vendem o item
     */
    List<ItemStore> findByItemId(Integer itemId);
}
