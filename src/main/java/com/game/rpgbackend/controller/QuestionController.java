package com.game.rpgbackend.controller;

import com.game.rpgbackend.domain.Question;
import com.game.rpgbackend.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller responsável pelas operações de questões educacionais.
 */
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    /**
     * Busca uma questão aleatória baseada na dificuldade e nível do jogador.
     */
    @GetMapping("/random")
    public ResponseEntity<Question> getRandomQuestion(
            @RequestParam String difficulty,
            @RequestParam Integer playerLevel,
            @RequestParam(required = false) Integer contentId) {

        Question question = questionService.getRandomQuestion(difficulty, playerLevel, contentId);
        return ResponseEntity.ok(question);
    }

    /**
     * Busca questões por conteúdo.
     */
    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<Question>> getQuestionsByContent(@PathVariable Integer contentId) {
        List<Question> questions = questionService.getQuestionsByContent(contentId);
        return ResponseEntity.ok(questions);
    }

    /**
     * Verifica se uma resposta está correta.
     */
    @PostMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkAnswer(@RequestBody Map<String, Object> request) {
        Integer questionId = (Integer) request.get("questionId");
        String answer = (String) request.get("answer");

        boolean isCorrect = questionService.checkAnswer(questionId, answer);
        return ResponseEntity.ok(Map.of("correct", isCorrect));
    }
}

