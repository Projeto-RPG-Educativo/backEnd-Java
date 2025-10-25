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
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final Random random = new Random();

    /**
     * Busca uma pergunta aleatória baseada na dificuldade e nível do jogador.
     * Remove a resposta correta do objeto antes de retornar.
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
     * Busca uma pergunta baseada em múltiplas dificuldades.
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
     * Busca questões por conteúdo.
     */
    public List<Question> getQuestionsByContent(Integer contentId) {
        return questionRepository.findByContentId(contentId);
    }

    /**
     * Busca uma questão por ID (inclui resposta correta - usar apenas no backend).
     */
    public Question getQuestionById(Integer questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Questão não encontrada"));
    }

    /**
     * Verifica se uma resposta está correta.
     */
    public boolean checkAnswer(Integer questionId, String answer) {
        Question question = getQuestionById(questionId);
        return question.getCorrectAnswer().trim().equalsIgnoreCase(answer.trim());
    }
}
