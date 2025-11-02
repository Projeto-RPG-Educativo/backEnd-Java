package com.game.rpgbackend.controller.question;

import com.game.rpgbackend.domain.Question;
import com.game.rpgbackend.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.game.rpgbackend.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import com.game.rpgbackend.dto.request.question.CheckAnswerRequest;
import com.game.rpgbackend.dto.response.question.CheckAnswerResponse;
import com.game.rpgbackend.dto.response.question.QuestionDto;
import jakarta.validation.Valid;
import java.util.stream.Collectors;

/**
 * Controller REST responsável pelas operações de questões educacionais.
 * <p>
 * Fornece endpoints para buscar questões de inglês, verificar respostas
 * e obter dicas (habilidade especial da classe Ladino).
 * As questões são filtradas por dificuldade, nível do jogador e conteúdo.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    /**
     * Busca uma questão aleatória adaptada ao nível e dificuldade.
     * <p>
     * A questão é selecionada aleatoriamente respeitando os filtros fornecidos.
     * Pode ser filtrada opcionalmente por um conteúdo educacional específico.
     * </p>
     *
     * @param difficulty nível de dificuldade (easy, medium, hard)
     * @param playerLevel nível atual do jogador para filtrar questões apropriadas
     * @param contentId (opcional) ID do conteúdo para filtrar questões relacionadas
     * @return questão aleatória sem revelar a resposta correta
     */
    @GetMapping("/random")
    public ResponseEntity<QuestionDto> getRandomQuestion(
            @RequestParam String difficulty,
            @RequestParam Integer playerLevel,
            @RequestParam(required = false) Integer contentId) {

        Question question = questionService.getRandomQuestion(difficulty, playerLevel, contentId);
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        dto.setDifficulty(question.getDifficulty());
        dto.setQuestionContent(question.getQuestionContent());
        dto.setMinLevel(question.getMinLevel());
        dto.setHint(question.getHint());
        dto.setContentId(question.getContent().getId());
        return ResponseEntity.ok(dto);
    }

    /**
     * Busca todas as questões relacionadas a um conteúdo educacional específico.
     *
     * @param contentId identificador único do conteúdo educacional
     * @return lista de questões do conteúdo especificado
     */
    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<QuestionDto>> getQuestionsByContent(@PathVariable Integer contentId) {
        List<Question> questions = questionService.getQuestionsByContent(contentId);
        List<QuestionDto> dtos = questions.stream().map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Converte entidade Question para DTO de resposta.
     *
     * @param question entidade de questão
     * @return DTO formatado para envio ao cliente
     */
    private QuestionDto mapToDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        dto.setDifficulty(question.getDifficulty());
        dto.setQuestionContent(question.getQuestionContent());
        dto.setMinLevel(question.getMinLevel());
        dto.setHint(question.getHint());
        dto.setContentId(question.getContent().getId());
        return dto;
    }

    /**
     * Verifica se uma resposta fornecida está correta.
     * <p>
     * Compara a resposta do jogador com a resposta correta da questão.
     * </p>
     *
     * @param request dados contendo ID da questão e resposta fornecida
     * @return indicação se a resposta está correta ou incorreta
     */
    @PostMapping("/check")
    public ResponseEntity<CheckAnswerResponse> checkAnswer(@Valid @RequestBody CheckAnswerRequest request) {
        Integer questionId = request.getQuestionId();
        String answer = request.getAnswer();

        boolean isCorrect = questionService.checkAnswer(questionId, answer);
        CheckAnswerResponse response = new CheckAnswerResponse(isCorrect);
        return ResponseEntity.ok(response);
    }

    /**
     * Retorna uma dica para auxiliar na resposta da questão.
     * <p>
     * Esta funcionalidade é uma habilidade especial da classe Ladino.
     * TODO: Implementar verificação de classe e restrições de uso.
     * </p>
     *
     * @param id identificador único da questão
     * @return texto da dica ou mensagem padrão se não houver dica disponível
     * @throws NotFoundException se a questão não for encontrada
     */
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
}
