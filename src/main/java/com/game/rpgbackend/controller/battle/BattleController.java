package com.game.rpgbackend.controller.battle;

import com.game.rpgbackend.dto.request.battle.BattleActionRequest;
import com.game.rpgbackend.dto.request.battle.StartBattleRequest;
import com.game.rpgbackend.dto.request.battle.SubmitAnswerRequest;
import com.game.rpgbackend.dto.response.battle.BattleStateResponse;
import com.game.rpgbackend.service.battle.BattleService;
import com.game.rpgbackend.util.AuthenticationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


/**
 * Controller REST responsável pelas operações de batalha.
 * <p>
 * Gerencia o sistema de batalhas do jogo, incluindo início de batalhas,
 * respostas a questões, ações de combate e consulta de estado.
 * Todas as batalhas incluem questões educacionais que devem ser respondidas.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/battle")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * Inicia uma nova batalha contra um monstro específico.
     * <p>
     * Cria uma instância de batalha com o monstro escolhido e
     * nível de dificuldade especificado. A primeira questão
     * é apresentada imediatamente.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param request dados da batalha (ID do monstro e dificuldade)
     * @return estado inicial da batalha com a primeira questão
     */
    @PostMapping("/start")
    public ResponseEntity<BattleStateResponse> startBattle(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody StartBattleRequest request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());

        BattleStateResponse battleState = battleService.startBattle(
            userId,
            request.getMonsterId(),
            request.getDifficulty(),
            request.getCharacterId()
        );

        return ResponseEntity.ok(battleState);
    }

    /**
     * Processa a resposta do jogador a uma questão durante a batalha.
     * <p>
     * Valida a resposta fornecida e aplica os efeitos no combate:
     * - Resposta correta: personagem ataca o monstro
     * - Resposta incorreta: monstro causa dano ao personagem
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param request dados da resposta (ID da batalha, questão e resposta)
     * @return novo estado da batalha após processar a resposta
     */
    @PostMapping("/answer")
    public ResponseEntity<BattleStateResponse> submitAnswer(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody SubmitAnswerRequest request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());

        BattleStateResponse newBattleState = battleService.processAnswer(
            userId,
            request.getBattleId(),
            request.getQuestionId(),
            request.getAnswer()
        );

        return ResponseEntity.ok(newBattleState);
    }

    /**
     * Executa uma ação de batalha específica.
     * <p>
     * Ações disponíveis:
     * - attack: ataque básico ao monstro
     * - defend: postura defensiva para reduzir dano recebido
     * - useSkill: usa habilidade especial da classe do personagem
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param request tipo de ação a ser executada
     * @return estado atualizado da batalha após a ação
     * @throws IllegalArgumentException se a ação for inválida
     */
    @PostMapping("/action")
    public ResponseEntity<BattleStateResponse> performAction(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BattleActionRequest request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());

        BattleStateResponse battleState;

        switch (request.getAction().toLowerCase()) {
            case "attack":
                battleState = battleService.attack(userId);
                break;
            case "defend":
                battleState = battleService.defend(userId);
                break;
            case "useskill":
            case "skill":
                battleState = battleService.useSkill(userId);
                break;
            default:
                throw new IllegalArgumentException("Ação inválida: " + request.getAction());
        }

        return ResponseEntity.ok(battleState);
    }

    /**
     * Retorna o estado atual da batalha ativa do usuário.
     * <p>
     * Permite consultar o progresso da batalha em andamento,
     * incluindo HP de ambos os combatentes e questão atual.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @return estado atual da batalha ou 204 No Content se não houver batalha ativa
     */
    @GetMapping("/current")
    public ResponseEntity<BattleStateResponse> getCurrentBattle(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        BattleStateResponse battleState = battleService.getActiveBattle(userId);

        if (battleState == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(battleState);
    }

    /**
     * Executa o turno do monstro após a ação do jogador.
     * <p>
     * Este endpoint deve ser chamado após o jogador realizar uma ação
     * (atacar, defender, usar skill) para processar a resposta do monstro.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @return estado atualizado da batalha após o turno do monstro
     */
    @PostMapping("/monster-turn")
    public ResponseEntity<BattleStateResponse> executeMonsterTurn(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        BattleStateResponse battleState = battleService.executeMonsterTurn(userId);

        return ResponseEntity.ok(battleState);
    }

    /**
     * Passa o turno do jogador quando está incapacitado (atordoado/STUN).
     * <p>
     * Este endpoint permite que o jogador passe o turno quando não pode
     * realizar nenhuma ação devido a efeitos como atordoamento.
     * O monstro executa seu turno e os efeitos são atualizados.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @return estado atualizado da batalha após passar o turno
     */
    @PostMapping("/skip-turn")
    public ResponseEntity<BattleStateResponse> skipTurn(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        BattleStateResponse battleState = battleService.skipTurn(userId);

        return ResponseEntity.ok(battleState);
    }
}
