package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa uma loja no jogo.
 * <p>
 * Lojas são estabelecimentos onde jogadores podem comprar itens.
 * Cada loja possui um inventário de itens disponíveis para venda.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "loja")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    /** Identificador único da loja */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Itens disponíveis para compra nesta loja.
     * Relacionamento OneToMany através de ItemLoja.
     */
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<ItemStore> items;
}
