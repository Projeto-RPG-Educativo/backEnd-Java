package com.game.rpgbackend.controller.achievement;

import com.game.rpgbackend.domain.Achievement;
import com.game.rpgbackend.dto.response.achievement.AchievementCompletionResponse;
import com.game.rpgbackend.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciar as conquistas (achievements) dos personagens.
 * <p>
 * Disponibiliza endpoints para consultar conquistas, verificar progresso
 * e obter estatísticas de conclusão.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    /**
     * Busca todas as conquistas de um personagem.
     *
     * @param characterId ID do personagem
     * @return lista de todas as conquistas
     */
    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<Achievement>> getCharacterAchievements(@PathVariable Long characterId) {
        List<Achievement> achievements = achievementService.getCharacterAchievements(characterId);
        return ResponseEntity.ok(achievements);
    }

    /**
     * Busca apenas as conquistas completadas de um personagem.
     *
     * @param characterId ID do personagem
     * @return lista de conquistas completadas
     */
    @GetMapping("/character/{characterId}/completed")
    public ResponseEntity<List<Achievement>> getCompletedAchievements(@PathVariable Long characterId) {
        List<Achievement> achievements = achievementService.getCompletedAchievements(characterId);
        return ResponseEntity.ok(achievements);
    }

    /**
     * Busca apenas as conquistas em progresso de um personagem.
     *
     * @param characterId ID do personagem
     * @return lista de conquistas em progresso
     */
    @GetMapping("/character/{characterId}/in-progress")
    public ResponseEntity<List<Achievement>> getInProgressAchievements(@PathVariable Long characterId) {
        List<Achievement> achievements = achievementService.getInProgressAchievements(characterId);
        return ResponseEntity.ok(achievements);
    }

    /**
     * Busca as conquistas desbloqueadas mais recentemente.
     *
     * @param characterId ID do personagem
     * @return lista de conquistas recentes
     */
    @GetMapping("/character/{characterId}/recent")
    public ResponseEntity<List<Achievement>> getRecentlyUnlockedAchievements(@PathVariable Long characterId) {
        List<Achievement> achievements = achievementService.getRecentlyUnlockedAchievements(characterId);
        return ResponseEntity.ok(achievements);
    }

    /**
     * Retorna o percentual de conclusão de todas as conquistas.
     *
     * @param characterId ID do personagem
     * @return percentual de conquistas completadas
     */
    @GetMapping("/character/{characterId}/completion")
    public ResponseEntity<AchievementCompletionResponse> getCompletionPercentage(@PathVariable Long characterId) {
        double percentage = achievementService.getCompletionPercentage(characterId);
        long completed = achievementService.getCompletedAchievements(characterId).size();
        long total = achievementService.getCharacterAchievements(characterId).size();

        AchievementCompletionResponse response = new AchievementCompletionResponse(percentage, completed, total);
        return ResponseEntity.ok(response);
    }
}
