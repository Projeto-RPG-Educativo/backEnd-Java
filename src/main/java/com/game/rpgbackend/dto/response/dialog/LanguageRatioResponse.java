package com.game.rpgbackend.dto.response.dialog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para proporção de idioma em diálogos educacionais.
 * <p>
 * Retorna a proporção calculada de inglês/português que deve ser
 * exibida em um diálogo educacional baseado no nível do personagem.
 * </p>
 * <p>
 * Sistema de progressão:
 * - Valor 0.2 (20%): Nível mínimo - 20% inglês, 80% português
 * - A cada nível acima do mínimo: +20% inglês
 * - Valor 1.0 (100%): Nível alto - 100% inglês
 * </p>
 * <p>
 * Usado para implementar aprendizado progressivo de idiomas,
 * aumentando gradualmente a exposição ao inglês conforme o jogador evolui.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageRatioResponse {

    /**
     * Proporção de inglês a ser exibida no diálogo.
     * Valor entre 0.2 (20%) e 1.0 (100%).
     */
    private Double ratio;
}
