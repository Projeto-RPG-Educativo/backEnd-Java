package com.game.rpgbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa um usuário do sistema.
 * <p>
 * Um usuário pode ter múltiplos personagens, saves, estatísticas,
 * conquistas e histórico de batalhas. Esta é a entidade central
 * que gerencia todas as informações relacionadas ao jogador.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Identificador único do usuário (auto-incrementado).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nome de usuário único para login e identificação no sistema.
     */
    @Column(name = "nome_usuario", unique = true, nullable = false)
    private String nomeUsuario;

    /**
     * Email único do usuário para comunicação e recuperação de conta.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Hash da senha do usuário criptografada (nunca armazenar senha em texto plano).
     */
    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    /**
     * Data e hora de criação da conta do usuário.
     */
    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    /**
     * Lista de personagens que pertencem a este usuário.
     * Relacionamento OneToMany - um usuário pode ter vários personagens.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Character> characters;

    /**
     * Lista de saves (salvamentos de jogo) do usuário.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameSave> saves;

    /**
     * Estatísticas do jogador (relação 1:1).
     * Um usuário tem apenas uma instância de estatísticas.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private PlayerStats stats;

    /**
     * Lista de conquistas desbloqueadas pelo usuário.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Achievement> achievements;

    /**
     * Histórico de batalhas do usuário.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BattleHistory> battles;
}
