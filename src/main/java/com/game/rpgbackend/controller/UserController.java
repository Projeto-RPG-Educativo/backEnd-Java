package com.game.rpgbackend.controller;

import com.game.rpgbackend.dto.request.UpdateProfileRequest;
import com.game.rpgbackend.dto.response.UserProfileResponse;
import com.game.rpgbackend.dto.response.UserStatsResponse;
import com.game.rpgbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST responsável pelas operações de perfil do usuário.
 * <p>
 * Fornece endpoints para consultar e atualizar informações do perfil,
 * além de visualizar estatísticas de jogo do usuário autenticado.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retorna o perfil completo do usuário autenticado.
     * <p>
     * Inclui informações pessoais e estatísticas básicas como
     * total de personagens criados.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @return dados do perfil do usuário
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserProfileResponse profile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    /**
     * Atualiza as informações de perfil do usuário.
     * <p>
     * Permite atualizar nome, email e/ou senha. Todos os campos são opcionais.
     * Para alterar a senha, a senha atual deve ser fornecida.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param request dados a serem atualizados
     * @return perfil atualizado do usuário
     */
    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileRequest request) {
        UserProfileResponse updated = userService.updateProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Retorna as estatísticas de jogo do usuário autenticado.
     * <p>
     * Inclui dados como nível, batalhas vencidas/perdidas,
     * questões corretas/erradas e taxa de acerto.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @return estatísticas completas de jogo do usuário
     */
    @GetMapping("/stats")
    public ResponseEntity<UserStatsResponse> getStats(@AuthenticationPrincipal UserDetails userDetails) {
        UserStatsResponse stats = userService.getUserStats(userDetails.getUsername());
        return ResponseEntity.ok(stats);
    }
}
