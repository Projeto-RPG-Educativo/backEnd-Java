package com.game.rpgbackend.controller.hub;

import com.game.rpgbackend.domain.*;
import com.game.rpgbackend.dto.request.hub.PurchaseItemRequest;
import com.game.rpgbackend.dto.request.hub.PurchaseSkillRequest;
import com.game.rpgbackend.dto.response.hub.BookDto;
import com.game.rpgbackend.dto.response.hub.SkillDto;
import com.game.rpgbackend.service.hub.*;
import com.game.rpgbackend.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<BookDto>> getBooks() {
        List<Book> books = libraryService.getAvailableBooks();
        List<BookDto> dtos = books.stream().map(this::mapToBookDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/library/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Integer id) {
        Book book = libraryService.getBookById(id);
        BookDto dto = mapToBookDto(book);
        return ResponseEntity.ok(dto);
    }

    // === TORRE DO CONHECIMENTO ===

    @GetMapping("/tower/skills")
    public ResponseEntity<List<SkillDto>> getSkills() {
        List<Skill> skills = towerService.getAvailableSkills();
        List<SkillDto> dtos = skills.stream().map(this::mapToSkillDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
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
            @Valid @RequestBody PurchaseSkillRequest request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        Integer skillId = request.getSkillId();

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
            @Valid @RequestBody PurchaseItemRequest request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        Integer lojaId = request.getLojaId();
        Integer itemId = request.getItemId();

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

    private BookDto mapToBookDto(Book book) {
        return new BookDto(book.getId(), book.getBookTitle(), book.getContent(), book.getType(), book.getDifficulty());
    }

    private SkillDto mapToSkillDto(Skill skill) {
        return new SkillDto(skill.getId(), skill.getName(), skill.getDescription(), skill.getCost(), skill.getType(), skill.getEffect());
    }
}
