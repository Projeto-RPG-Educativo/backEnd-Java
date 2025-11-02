package com.game.rpgbackend.dto.response.character;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO de resposta para informações completas do personagem.
 * <p>
 * Retorna todos os dados relevantes de um personagem incluindo atributos,
 * recursos, classe e informações do jogador, sem referências circulares.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterDTO {
    /** ID único do personagem */
    private Integer id;

    /** Nome do personagem */
    private String nome;

    /** Pontos de experiência acumulados */
    private Integer xp;

    /** Quantidade de ouro (moeda do jogo) */
    private Integer gold;

    /** Pontos de vida atuais */
    private Integer hp;

    /** Energia atual do personagem */
    private Integer energy;

    /** Energia máxima do personagem */
    private Integer maxEnergy;

    /** Data e hora do último salvamento */
    private LocalDateTime lastSavedAt;

    /** ID do usuário proprietário */
    private Integer userId;

    /** Nome do usuário proprietário */
    private String userName;

    /** ID da classe do personagem */
    private Integer classId;

    /** Nome da classe do personagem */
    private String className;
}
