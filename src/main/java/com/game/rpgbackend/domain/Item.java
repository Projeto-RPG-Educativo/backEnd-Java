package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column
    private Integer value;

    @Column
    private String description;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<InventoryItem> inventories;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemLoja> lojas;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<QuestRewardItem> reward;
}
