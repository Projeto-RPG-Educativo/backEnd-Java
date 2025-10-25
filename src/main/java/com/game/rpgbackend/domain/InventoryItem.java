package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa a relação entre um inventário e seus itens.
 * <p>
 * Esta tabela de junção gerencia a quantidade de cada item específico
 * dentro de um inventário de personagem.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "inventory_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {

    /** Identificador único do registro */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Inventário que contém este item.
     * Relacionamento ManyToOne - um inventário pode ter vários itens.
     */
    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    /**
     * Item contido no inventário.
     * Relacionamento ManyToOne - um item pode estar em vários inventários.
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /** Quantidade deste item no inventário */
    @Column(nullable = false)
    private Integer quantity = 1;
}
