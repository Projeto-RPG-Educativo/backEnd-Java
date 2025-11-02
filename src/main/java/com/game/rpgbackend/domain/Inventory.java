package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa o inventário de um personagem.
 * <p>
 * Cada personagem possui um único inventário que armazena todos os seus itens.
 * O inventário gerencia a coleção de itens através da entidade InventoryItem.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    /** Identificador único do inventário */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Personagem dono deste inventário.
     * Relacionamento OneToOne - cada personagem tem um único inventário.
     */
    @OneToOne
    @JoinColumn(name = "character_id", nullable = false, unique = true)
    private Character character;

    /**
     * Lista de itens contidos neste inventário.
     * Relacionamento OneToMany através de InventoryItem.
     */
    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    private List<InventoryItem> items;
}
