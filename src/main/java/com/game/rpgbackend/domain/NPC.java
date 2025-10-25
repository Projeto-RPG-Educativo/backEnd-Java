package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidade que representa um personagem não-jogável (NPC) no jogo.
 * <p>
 * NPCs são personagens controlados pelo sistema que podem interagir
 * com o jogador através de diálogos, oferecendo quests, informações
 * ou serviços como vendas de itens.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "npc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NPC {

    /** Identificador único do NPC */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nome único do NPC */
    @Column(nullable = false, unique = true)
    private String name;

    /** Descrição e história do NPC */
    @Column(nullable = false)
    private String description;

    /** Tipo do NPC (mercador, guarda, mentor, etc.) */
    @Column(nullable = false)
    private String type;

    /** Localização onde o NPC pode ser encontrado */
    @Column
    private String location;

    /**
     * Diálogos que este NPC pode ter com o jogador.
     * Relacionamento OneToMany - um NPC pode ter vários diálogos.
     */
    @OneToMany(mappedBy = "npc", cascade = CascadeType.ALL)
    private List<Dialogue> dialogues;
}
