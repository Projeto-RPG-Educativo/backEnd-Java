package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "quest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "xp_ganho")
    private Integer xpGanho;

    @Column(name = "ouro_ganho")
    private Integer ouroGanho;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL)
    private List<CharacterQuest> characters;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL)
    private List<QuestRewardItem> reward;
}
