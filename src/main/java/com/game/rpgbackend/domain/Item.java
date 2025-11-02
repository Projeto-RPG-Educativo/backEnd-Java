package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa um item no jogo.
 * <p>
 * Itens podem ser encontrados em inventários, lojas e como recompensas de quests.
 * Cada item possui um tipo (arma, armadura, consumível, etc.) e valor.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    /** Identificador único do item */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nome do item */
    @Column(nullable = false)
    private String name;

    /** Tipo do item (arma, armadura, consumível, etc.) */
    @Column(nullable = false)
    private String type;

    /** Valor em ouro do item */
    @Column
    private Integer value;

    /** Descrição detalhada do item e seus efeitos */
    @Column
    private String description;

    /**
     * Inventários que contêm este item.
     * Relacionamento OneToMany através de InventoryItem.
     */
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<InventoryItem> inventories;

    /**
     * Lojas que vendem este item.
     * Relacionamento OneToMany através de ItemLoja.
     */
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemStore> stores;

    /**
     * Quests que dão este item como recompensa.
     */
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<QuestRewardItem> reward;
}
