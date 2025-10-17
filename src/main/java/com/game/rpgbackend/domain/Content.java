package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Entity
@Table(name = "content")
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "level_minimo", nullable = false)
    private Integer levelMinimo = 1;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<Question> questions;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<Dialog> dialogs;
}
