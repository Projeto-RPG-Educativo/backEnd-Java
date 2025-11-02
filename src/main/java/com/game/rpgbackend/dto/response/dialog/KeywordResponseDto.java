package com.game.rpgbackend.dto.response.dialog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para palavras-chave destacadas em diálogos educacionais.
 * <p>
 * Representa uma palavra importante no diálogo que possui tradução
 * e pode ser destacada visualmente para auxiliar no aprendizado de idiomas.
 * Palavras-chave ajudam o jogador a entender termos específicos sem
 * precisar consultar dicionários externos.
 * </p>
 * <p>
 * Uso no frontend:
 * - Palavras com destaque=true devem ser sublinhadas ou com cor diferente
 * - Ao passar o mouse ou clicar, exibir a tradução em tooltip
 * - Facilita aprendizado contextual de vocabulário
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordResponseDto {

    /**
     * Palavra no idioma original (inglês ou português).
     */
    private String palavra;

    /**
     * Tradução da palavra para o outro idioma.
     */
    private String traducao;

    /**
     * Indica se a palavra deve ser destacada visualmente no diálogo.
     */
    private Boolean destaque;
}
