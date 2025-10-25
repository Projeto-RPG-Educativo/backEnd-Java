package com.game.rpgbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO de resposta simplificado para listagem de personagens.
 * <p>
 * Versão reduzida do CharacterDTO contendo apenas informações
 * essenciais para exibição em listas, melhorando a performance.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterListDTO {
    /** ID único do personagem */
    private Integer id;
    
    /** Nome do personagem */
    private String nome;
    
    /** Pontos de experiência */
    private Integer xp;
    
    /** Quantidade de ouro */
    private Integer gold;
    
    /** Pontos de vida atuais */
    private Integer hp;
    
    /** Nome da classe */
    private String className;
}
