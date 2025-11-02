package com.game.rpgbackend.dto.response.dialog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de resposta para diálogos educacionais multilíngues.
 * <p>
 * Contém um diálogo completo em dois idiomas (português e inglês) com
 * palavras-chave destacadas para facilitar o aprendizado. O frontend
 * usa a proporção calculada pelo sistema para determinar quanto de cada
 * idioma exibir ao jogador baseado em seu nível.
 * </p>
 * <p>
 * Fluxo de uso:
 * 1. Backend envia diálogo completo em ambos os idiomas
 * 2. Backend calcula proporção baseada no nível do jogador (via LanguageRatioResponse)
 * 3. Frontend mescla os textos conforme a proporção (ex: 60% inglês, 40% português)
 * 4. Palavras-chave são destacadas para ajudar na compreensão
 * </p>
 * <p>
 * Exemplo de uso:
 * - Jogador nível 3: Exibe 60% do texto em inglês, 40% em português
 * - Palavras importantes ficam destacadas com tradução em tooltip
 * - Implementa aprendizado progressivo de idiomas
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DialogResponseDto {

    /**
     * Identificador único do diálogo.
     */
    private Integer id;

    /**
     * Texto completo do diálogo em português.
     * Usado como base quando proporção de inglês é baixa.
     */
    private String portugues;

    /**
     * Texto completo do diálogo em inglês.
     * Usado progressivamente conforme jogador evolui.
     */
    private String ingles;

    /**
     * Lista de palavras-chave importantes com traduções.
     * Cada palavra pode ser destacada visualmente para auxiliar na compreensão.
     */
    private List<KeywordResponseDto> keywords;
}
