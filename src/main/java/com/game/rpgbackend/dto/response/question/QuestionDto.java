package com.game.rpgbackend.dto.response.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para questões educacionais.
 * <p>
 * Representa uma questão completa com todas as opções de resposta,
 * sem incluir a resposta correta (que é mantida apenas no backend).
 * Usado em batalhas e no sistema educacional do jogo.
 * </p>
 * <p>
 * Estrutura da questão:
 * - Texto da pergunta
 * - 3 opções de resposta (A, B, C)
 * - Metadados (dificuldade, nível mínimo, categoria)
 * - Dica opcional (desbloqueada com habilidade do Ladino)
 * </p>
 * <p>
 * IMPORTANTE: A resposta correta NÃO deve ser enviada ao frontend
 * para evitar trapaças. Ela é validada apenas no backend.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {

    /**
     * Identificador único da questão.
     */
    private Integer id;

    /**
     * Texto da pergunta a ser exibida.
     */
    private String questionText;

    /**
     * Primeira opção de resposta (A).
     */
    private String optionA;

    /**
     * Segunda opção de resposta (B).
     */
    private String optionB;

    /**
     * Terceira opção de resposta (C).
     */
    private String optionC;

    /**
     * Resposta correta (A, B ou C).
     * ATENÇÃO: Este campo NÃO deve ser enviado ao frontend.
     * Incluído apenas para uso interno no backend.
     */
    private String correctAnswer;

    /**
     * Nível de dificuldade (easy, medium, hard).
     */
    private String difficulty;

    /**
     * Categoria/conteúdo da questão (gramática, vocabulário, etc.).
     */
    private String questionContent;

    /**
     * Nível mínimo do personagem para acessar esta questão.
     */
    private Integer minLevel;

    /**
     * Dica sobre a resposta correta (desbloqueada pela habilidade do Ladino).
     */
    private String hint;

    /**
     * ID do conteúdo educacional ao qual a questão pertence.
     */
    private Integer contentId;
}
