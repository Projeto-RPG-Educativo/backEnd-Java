package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa uma habilidade especial no jogo.
 * <p>
 * Habilidades podem ser desbloqueadas pelos jogadores e usadas em batalhas.
 * Cada habilidade possui um custo, tipo e efeito específico.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "skill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    /** Identificador único da habilidade */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nome único da habilidade */
    @Column(nullable = false, unique = true)
    private String name;

    /** Descrição detalhada da habilidade */
    @Column(nullable = false)
    private String description;

    /** Custo em pontos de habilidade para desbloquear */
    @Column(nullable = false)
    private Integer cost;

    /** Tipo da habilidade (ataque, defesa, suporte, etc.) */
    @Column(nullable = false)
    private String type;

    /** Efeito da habilidade quando usada */
    @Column(nullable = false)
    private String effect;

    /**
     * Jogadores que desbloquearam esta habilidade.
     * Relacionamento ManyToMany mapeado pela lista unlockedSkills em PlayerStats.
     */
    @ManyToMany(mappedBy = "unlockedSkills")
    private List<PlayerStats> players;
}
