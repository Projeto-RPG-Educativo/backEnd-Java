package com.game.rpgbackend.service.question;

import com.game.rpgbackend.domain.Question;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * Serviço responsável pela gestão de questões educacionais.
 * <p>
 * Gerencia o banco de questões do sistema, incluindo:
 * - Seleção aleatória de questões baseada em critérios
 * - Filtragem por dificuldade, nível e conteúdo
 * - Validação de respostas
 * - Fornecimento de dicas (habilidade do Ladino)
 * </p>
 * <p>
 * As questões são fundamentais para o sistema de batalhas,
 * onde acertar ou errar influencia o resultado do combate.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final Random random = new Random();

    /**
     * Busca uma questão aleatória baseada em critérios específicos.
     * <p>
     * A questão selecionada:
     * - Corresponde à dificuldade especificada
     * - É acessível para o nível do jogador (minLevel ≤ playerLevel)
     * - Opcionalmente filtrada por conteúdo educacional
     * </p>
     * <p>
     * A resposta correta NÃO é incluída no retorno para evitar trapaças.
     * </p>
     *
     * @param difficulty nível de dificuldade (easy, medium, hard)
     * @param playerLevel nível atual do jogador
     * @param contentId (opcional) ID do conteúdo para filtrar questões relacionadas
     * @return questão aleatória que atende aos critérios, sem a resposta correta
     * @throws NotFoundException se nenhuma questão adequada for encontrada
     */
    public Question getRandomQuestion(String difficulty, Integer playerLevel, Integer contentId) {
        // Normaliza a dificuldade (primeira letra maiúscula)
        String normalizedDifficulty = difficulty.substring(0, 1).toUpperCase() +
                difficulty.substring(1).toLowerCase();

        // Busca perguntas adequadas ao nível do jogador
        List<Question> questions;
        if (contentId != null) {
            questions = questionRepository.findByDifficultyAndMinLevelLessThanEqualAndContentId(
                    normalizedDifficulty, playerLevel, contentId
            );
        } else {
            questions = questionRepository.findByDifficultyAndMinLevelLessThanEqual(
                    normalizedDifficulty, playerLevel
            );
        }

        if (questions.isEmpty()) {
            throw new NotFoundException(
                    String.format("Nenhuma pergunta encontrada para dificuldade %s e nível %d.",
                            normalizedDifficulty, playerLevel)
            );
        }

        // Seleciona uma pergunta aleatória
        int randomIndex = random.nextInt(questions.size());
        Question randomQuestion = questions.get(randomIndex);

        // Log para debug
        System.out.println(String.format(
                "Pergunta selecionada: ID=%d, Dificuldade=%s, NivelMinimo=%d, PlayerLevel=%d",
                randomQuestion.getId(),
                randomQuestion.getDifficulty(),
                randomQuestion.getMinLevel(),
                playerLevel
        ));

        // Nota: A resposta correta NÃO deve ser enviada para o frontend.
        // Isso deve ser tratado no Controller/DTO layer
        return randomQuestion;
    }

    /**
     * Busca uma questão aleatória de múltiplos níveis de dificuldade.
     * <p>
     * Útil para batalhas que aceitam questões de diferentes dificuldades
     * ou para variação no desafio apresentado ao jogador.
     * </p>
     *
     * @param difficulties lista de níveis de dificuldade aceitos
     * @param playerLevel nível atual do jogador
     * @return questão aleatória de uma das dificuldades especificadas
     * @throws NotFoundException se nenhuma questão adequada for encontrada
     */
    public Question getQuestionByDifficultyRange(List<String> difficulties, Integer playerLevel) {
        long questionCount = questionRepository.countByDifficultyInAndMinLevelLessThanEqual(
                difficulties, playerLevel
        );

        if (questionCount == 0) {
            throw new NotFoundException("Nenhuma pergunta encontrada para as dificuldades especificadas.");
        }

        int skip = random.nextInt((int) questionCount);

        List<Question> questions = questionRepository.findByDifficultyInAndMinLevelLessThanEqual(
                difficulties, playerLevel
        );

        if (questions.isEmpty() || skip >= questions.size()) {
            throw new NotFoundException("Não foi possível buscar uma pergunta.");
        }

        return questions.get(skip);
    }

    /**
     * Busca todas as questões relacionadas a um conteúdo educacional específico.
     *
     * @param contentId identificador do conteúdo educacional
     * @return lista de questões do conteúdo especificado
     */
    public List<Question> getQuestionsByContent(Integer contentId) {
        return questionRepository.findByContentId(contentId);
    }

    /**
     * Busca uma questão completa por ID, incluindo a resposta correta.
     * <p>
     * ATENÇÃO: Este método retorna a resposta correta e deve ser usado
     * apenas em operações backend. Nunca enviar diretamente ao frontend.
     * </p>
     *
     * @param questionId identificador único da questão
     * @return questão completa com resposta correta
     * @throws NotFoundException se a questão não for encontrada
     */
    public Question getQuestionById(Integer questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Questão não encontrada"));
    }

    /**
     * Valida se uma resposta fornecida está correta.
     * <p>
     * A comparação é case-insensitive e ignora espaços em branco extras.
     * </p>
     *
     * @param questionId identificador da questão
     * @param answer resposta fornecida pelo jogador
     * @return true se a resposta estiver correta, false caso contrário
     * @throws NotFoundException se a questão não for encontrada
     */
    public boolean checkAnswer(Integer questionId, String answer) {
        Question question = getQuestionById(questionId);
        return question.getCorrectAnswer().trim().equalsIgnoreCase(answer.trim());
    }

    /**
     * Busca a dica de uma questão específica.
     * <p>
     * Este método é usado pela habilidade "Fraqueza" da classe Ladino.
     * Retorna uma dica que ajuda o jogador a encontrar a resposta correta.
     * </p>
     * <p>
     * TODO: Implementar verificações adicionais:
     * - Confirmar que o personagem é da classe Ladino
     * - Verificar cooldown da habilidade
     * - Descontar custo de energia
     * </p>
     *
     * @param questionId identificador da questão
     * @return texto da dica ou null se não houver dica disponível
     * @throws NotFoundException se a questão não for encontrada
     */
    public String getHintForQuestion(Integer questionId) {
        Question question = getQuestionById(questionId); // Reutiliza o método que já busca e lança exceção

        // TODO: Aqui pode ser adicionada lógica futura, como:
        // - Verificar se o personagem atual (obtido do contexto de segurança) é Ladino.
        // - Verificar cooldown da habilidade.
        // - Descontar custo de mana/energia do personagem.
        // - Lançar uma exceção customizada (ex: SkillNotAvailableException) se não puder usar.

        return question.getHint(); // Retorna o valor do campo hint (pode ser null)
    }
}
