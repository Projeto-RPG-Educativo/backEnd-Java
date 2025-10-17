package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "loja")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL)
    private List<ItemLoja> items;
}
