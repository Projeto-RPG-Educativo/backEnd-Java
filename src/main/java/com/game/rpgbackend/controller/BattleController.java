package com.game.rpgbackend.controller;

import com.game.rpgbackend.dto.request.BattleActionRequest;
import com.game.rpgbackend.dto.request.StartBattleRequest;
import com.game.rpgbackend.dto.request.SubmitAnswerRequest;
import com.game.rpgbackend.dto.response.BattleStateResponse;
import com.game.rpgbackend.service.battle.BattleService;
import com.game.rpgbackend.util.AuthenticationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelas operações de batalha.
 */
@RestController
@RequestMapping("/api/battle")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * Inicia uma nova batalha.
     */
    @PostMapping("/start")
    public ResponseEntity<BattleStateResponse> startBattle(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody StartBattleRequest request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());

        BattleStateResponse battleState = battleService.startBattle(
            userId,
            request.getMonsterId(),
            request.getDifficulty()
        );

        return ResponseEntity.ok(battleState);
    }

    /**
     * Processa a resposta do jogador a uma questão.
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
     * Executa uma ação de batalha (attack, defend, useSkill).
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
     * Retorna o estado atual da batalha.
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
}
