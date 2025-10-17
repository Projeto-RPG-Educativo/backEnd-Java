package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "npc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NPC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String type;

    @Column
    private String location;

    @OneToMany(mappedBy = "npc", cascade = CascadeType.ALL)
    private List<Dialogue> dialogues;
}
