package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "dialog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @Column(nullable = false)
    private String portugues;

    @Column(nullable = false)
    private String ingles;

    @Column(name = "level_minimo", nullable = false)
    private Integer levelMinimo = 1;

    @OneToMany(mappedBy = "dialog", cascade = CascadeType.ALL)
    private List<DialogKeyword> keywords;
}
