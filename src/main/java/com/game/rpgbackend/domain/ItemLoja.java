package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Entidade que representa itens disponíveis em uma loja.
 * <p>
 * Esta tabela de junção gerencia o estoque de itens em lojas,
 * incluindo preço e quantidade disponível para compra.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "item_loja")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ItemLojaId.class)
public class ItemLoja {

    /** ID da loja (parte da chave composta) */
    @Id
    @Column(name = "loja_id")
    private Integer lojaId;

    /** ID do item (parte da chave composta) */
    @Id
    @Column(name = "item_id")
    private Integer itemId;

    /**
     * Loja que vende este item.
     * Relacionamento ManyToOne.
     */
    @ManyToOne
    @JoinColumn(name = "loja_id", insertable = false, updatable = false)
    private Loja loja;

    /**
     * Item sendo vendido.
     * Relacionamento ManyToOne.
     */
    @ManyToOne
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private Item item;

    /** Preço de venda do item em ouro */
    @Column(nullable = false)
    private Integer preco;

    /** Quantidade disponível em estoque */
    @Column(nullable = false)
    private Integer quantidade;

}
