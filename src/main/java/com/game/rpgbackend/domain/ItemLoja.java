package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "item_loja")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ItemLojaId.class)
public class ItemLoja {

    @Id
    @Column(name = "loja_id")
    private Integer lojaId;

    @Id
    @Column(name = "item_id")
    private Integer itemId;

    @ManyToOne
    @JoinColumn(name = "loja_id", insertable = false, updatable = false)
    private Loja loja;

    @ManyToOne
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private Item item;

    @Column(nullable = false)
    private Integer preco;

    @Column(nullable = false)
    private Integer quantidade;

}
