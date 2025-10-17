package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dialog_keyword")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DialogKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dialog_id", nullable = false)
    private Dialog dialog;

    @Column(nullable = false)
    private String palavra;

    @Column(nullable = false)
    private String traducao;

    @Column(nullable = false)
    private Boolean destaque = true;
}
