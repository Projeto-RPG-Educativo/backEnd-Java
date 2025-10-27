package com.game.rpgbackend.controller;

import com.game.rpgbackend.domain.Question;
import com.game.rpgbackend.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.game.rpgbackend.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

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
    @GetMapping("/{id}/hint")
    @PreAuthorize("isAuthenticated()") // Garante que apenas usuarios logados acessem
    public ResponseEntity<String> getQuestionHint(@PathVariable Integer id) {
        // TODO: Implementar lógica de segurança adicional:
        // 1. Obter o personagem atual do usuário logado.
        // 2. Verificar se a classe do personagem é "Ladino".
        // 3. Verificar se a habilidade não está em cooldown ou se tem custo (mana/energia).
        // 4. Se não for Ladino ou a habilidade não puder ser usada, retornar erro 403 Forbidden.

        try {
            String hint = questionService.getHintForQuestion(id);
            // Retorna a dica ou uma mensagem padrão se a dica for nula/vazia
            return ResponseEntity.ok(hint != null && !hint.isEmpty() ? hint : "Nenhuma dica disponível para esta pergunta.");
        } catch (NotFoundException e) {
            // Se a pergunta com o ID fornecido não existir
            return ResponseEntity.notFound().build();
        }
        // catch (AccessDeniedException e) { // Se a lógica de Ladino falhar
        //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas Ladinos podem usar esta habilidade.");
        // }
        catch (Exception e) {
            // Captura qualquer outro erro inesperado
            System.err.println("Erro ao buscar dica para questão ID " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a solicitação da dica.");
        }
    }
    // --- FIM DO NOVO MÉTODO ---
}


