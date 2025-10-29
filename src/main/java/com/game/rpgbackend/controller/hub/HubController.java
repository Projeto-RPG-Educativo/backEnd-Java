package com.game.rpgbackend.controller.hub;

import com.game.rpgbackend.domain.*;
import com.game.rpgbackend.service.hub.*;
import com.game.rpgbackend.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller responsável pelos locais do Hub (Biblioteca, Torre, Loja, etc).
 */
@RestController
@RequestMapping("/api/hub")
@RequiredArgsConstructor
public class HubController {

    private final LibraryService libraryService;
    private final TowerService towerService;
    private final StoreService storeService;
    private final StageService stageService;
    private final PlayerService playerService;
    private final AuthenticationUtil authenticationUtil;

    // === BIBLIOTECA SILENCIOSA ===

    @GetMapping("/library/books")
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(libraryService.getAvailableBooks());
    }

    @GetMapping("/library/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Integer id) {
        return ResponseEntity.ok(libraryService.getBookById(id));
    }

    // === TORRE DO CONHECIMENTO ===

    @GetMapping("/tower/skills")
    public ResponseEntity<List<Skill>> getSkills() {
        return ResponseEntity.ok(towerService.getAvailableSkills());
    }

    @GetMapping("/tower/content")
    public ResponseEntity<List<TowerService.ContentWithProgress>> getContent(
            @RequestParam Integer playerLevel) {
        return ResponseEntity.ok(towerService.getAvailableContent(playerLevel));
    }

    @GetMapping("/tower/content/{id}")
    public ResponseEntity<TowerService.ContentWithProgress> getContentById(@PathVariable Integer id) {
        return ResponseEntity.ok(towerService.getContentById(id));
    }

    @PostMapping("/tower/skills/purchase")
    public ResponseEntity<Skill> purchaseSkill(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Integer> request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        Integer skillId = request.get("skillId");

        Skill skill = towerService.purchaseSkill(userId, skillId);
        return ResponseEntity.ok(skill);
    }

    // === SEBO DA LINGUÍSTICA (LOJA) ===

    @GetMapping("/store/shops")
    public ResponseEntity<List<Store>> getStores() {
        return ResponseEntity.ok(storeService.getStores());
    }

    @GetMapping("/store/{lojaId}/items")
    public ResponseEntity<List<ItemStore>> getStoreItems(@PathVariable Integer lojaId) {
        return ResponseEntity.ok(storeService.getStoreItems(lojaId));
    }

    @PostMapping("/store/purchase")
    public ResponseEntity<StoreService.PurchaseResult> purchaseItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Integer> request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        Integer lojaId = request.get("lojaId");
        Integer itemId = request.get("itemId");

        StoreService.PurchaseResult result = storeService.purchaseStoreItem(userId, lojaId, itemId);
        return ResponseEntity.ok(result);
    }

    // === PALCO DA RETÓRICA ===

    @GetMapping("/stage/npcs")
    public ResponseEntity<List<NPC>> getNPCs() {
        return ResponseEntity.ok(stageService.getNPCs());
    }

    @GetMapping("/stage/npcs/{npcId}/dialogues")
    public ResponseEntity<List<Dialogue>> getNPCDialogues(@PathVariable Integer npcId) {
        return ResponseEntity.ok(stageService.getNPCDialogues(npcId));
    }

    // === ESTATÍSTICAS DO JOGADOR ===

    @GetMapping("/player/stats")
    public ResponseEntity<PlayerStats> getPlayerStats(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        return ResponseEntity.ok(playerService.getPlayerStats(userId));
    }

    @GetMapping("/player/achievements")
    public ResponseEntity<List<Achievement>> getAchievements(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        return ResponseEntity.ok(playerService.getAchievements(userId));
    }

    @GetMapping("/player/battle-history")
    public ResponseEntity<List<PlayerService.BattleHistoryResponse>> getBattleHistory(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        return ResponseEntity.ok(playerService.getBattleHistory(userId));
    }

    @GetMapping("/player/rankings")
    public ResponseEntity<List<PlayerService.RankingResponse>> getRankings() {
        return ResponseEntity.ok(playerService.getRankings());
    }
}
