package com.game.rpgbackend.controller.hub;

import com.game.rpgbackend.domain.*;
import com.game.rpgbackend.dto.request.hub.AcceptQuestRequest;
import com.game.rpgbackend.dto.request.hub.PurchaseItemRequest;
import com.game.rpgbackend.dto.request.hub.PurchaseSkillRequest;
import com.game.rpgbackend.dto.response.hub.BookDto;
import com.game.rpgbackend.dto.response.hub.QuestDto;
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
 * Controller REST responsável pelos locais do Hub central do jogo.
 * <p>
 * O Hub é a área principal onde o jogador acessa diversos locais temáticos:
 * - Biblioteca Silenciosa: consulta de livros e conteúdos educacionais
 * - Torre do Conhecimento: aprendizado e compra de habilidades
 * - Sebo da Linguística: loja para compra de itens
 * - Palco da Retórica: interação com NPCs e diálogos
 * - Estatísticas do Jogador: conquistas, ranking e histórico de batalhas
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
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
    private final QuestService questService;
    private final AuthenticationUtil authenticationUtil;

    // === BIBLIOTECA SILENCIOSA ===

    /**
     * Retorna todos os livros disponíveis na Biblioteca Silenciosa.
     * <p>
     * Os livros contêm conteúdos educacionais sobre gramática inglesa
     * e são organizados por dificuldade.
     * </p>
     *
     * @return lista de todos os livros disponíveis
     */
    @GetMapping("/library/books")
    public ResponseEntity<List<BookDto>> getBooks() {
        List<Book> books = libraryService.getAvailableBooks();
        List<BookDto> dtos = books.stream().map(this::mapToBookDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Busca um livro específico por ID na biblioteca.
     *
     * @param id identificador único do livro
     * @return DTO completo do livro ou 404 se não encontrado
     */
    @GetMapping("/library/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Integer id) {
        Book book = libraryService.getBookById(id);
        BookDto dto = mapToBookDto(book);
        return ResponseEntity.ok(dto);
    }

    // === TORRE DO CONHECIMENTO ===

    /**
     * Retorna todas as habilidades disponíveis na Torre do Conhecimento.
     * <p>
     * Habilidades podem ser compradas com ouro e fornecem
     * vantagens em batalha ou no aprendizado.
     * </p>
     *
     * @return lista de todas as habilidades disponíveis para compra
     */
    @GetMapping("/tower/skills")
    public ResponseEntity<List<SkillDto>> getSkills() {
        List<Skill> skills = towerService.getAvailableSkills();
        List<SkillDto> dtos = skills.stream().map(this::mapToSkillDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retorna conteúdos educacionais disponíveis baseados no nível do jogador.
     *
     * @param playerLevel nível atual do jogador
     * @return lista de conteúdos com progresso do jogador
     */
    @GetMapping("/tower/content")
    public ResponseEntity<List<TowerService.ContentWithProgress>> getContent(
            @RequestParam Integer playerLevel) {
        return ResponseEntity.ok(towerService.getAvailableContent(playerLevel));
    }

    /**
     * Busca um conteúdo educacional específico por ID.
     *
     * @param id identificador único do conteúdo
     * @return conteúdo com progresso do jogador ou 404 se não encontrado
     */
    @GetMapping("/tower/content/{id}")
    public ResponseEntity<TowerService.ContentWithProgress> getContentById(@PathVariable Integer id) {
        return ResponseEntity.ok(towerService.getContentById(id));
    }

    /**
     * Realiza a compra de uma habilidade na Torre do Conhecimento.
     * <p>
     * Deduz o custo em ouro do personagem e adiciona a habilidade
     * ao seu repertório. Requer ouro suficiente.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param request dados da compra contendo ID da habilidade
     * @return habilidade comprada
     * @throws com.game.rpgbackend.exception.BadRequestException se não houver ouro suficiente
     */
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

    /**
     * Retorna todas as lojas disponíveis no Sebo da Linguística.
     *
     * @return lista com todas as lojas do jogo
     */
    @GetMapping("/store/shops")
    public ResponseEntity<List<Store>> getStores() {
        return ResponseEntity.ok(storeService.getStores());
    }

    /**
     * Retorna todos os itens disponíveis em uma loja específica.
     *
     * @param lojaId identificador único da loja
     * @return lista de itens com preços da loja especificada
     */
    @GetMapping("/store/{lojaId}/items")
    public ResponseEntity<List<ItemStore>> getStoreItems(@PathVariable Integer lojaId) {
        return ResponseEntity.ok(storeService.getStoreItems(lojaId));
    }

    /**
     * Realiza a compra de um item na loja.
     * <p>
     * Deduz o custo do personagem e adiciona o item ao inventário.
     * Requer ouro suficiente.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param request dados da compra (loja e item)
     * @return resultado da compra com item adquirido
     * @throws com.game.rpgbackend.exception.BadRequestException se não houver ouro suficiente
     */
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

    /**
     * Retorna todos os NPCs disponíveis no Palco da Retórica.
     *
     * @return lista com todos os NPCs do jogo
     */
    @GetMapping("/stage/npcs")
    public ResponseEntity<List<NPC>> getNPCs() {
        return ResponseEntity.ok(stageService.getNPCs());
    }

    /**
     * Retorna todos os diálogos de um NPC específico.
     * <p>
     * Os diálogos permitem interação e aprendizado através de conversas.
     * </p>
     *
     * @param npcId identificador único do NPC
     * @return lista de diálogos do NPC especificado
     */
    @GetMapping("/stage/npcs/{npcId}/dialogues")
    public ResponseEntity<List<Dialogue>> getNPCDialogues(@PathVariable Integer npcId) {
        return ResponseEntity.ok(stageService.getNPCDialogues(npcId));
    }

    // === ESTATÍSTICAS DO JOGADOR ===

    /**
     * Retorna as estatísticas gerais do jogador autenticado.
     * <p>
     * Inclui total de batalhas, vitórias, derrotas, perguntas respondidas,
     * taxa de acerto e outros indicadores de progresso.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @return estatísticas completas do jogador
     */
    @GetMapping("/player/stats")
    public ResponseEntity<PlayerStats> getPlayerStats(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        return ResponseEntity.ok(playerService.getPlayerStats(userId));
    }

    /**
     * Retorna todas as conquistas desbloqueadas pelo jogador.
     *
     * @param userDetails detalhes do usuário autenticado
     * @return lista de conquistas do jogador
     */
    @GetMapping("/player/achievements")
    public ResponseEntity<List<Achievement>> getAchievements(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        return ResponseEntity.ok(playerService.getAchievements(userId));
    }

    /**
     * Retorna o histórico completo de batalhas do jogador.
     * <p>
     * Inclui detalhes de cada batalha: monstro enfrentado, resultado,
     * dano causado/recebido, perguntas respondidas e recompensas.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @return lista de batalhas realizadas pelo jogador
     */
    @GetMapping("/player/battle-history")
    public ResponseEntity<List<PlayerService.BattleHistoryResponse>> getBattleHistory(
            @AuthenticationPrincipal UserDetails userDetails) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        return ResponseEntity.ok(playerService.getBattleHistory(userId));
    }

    /**
     * Retorna o ranking global dos jogadores.
     * <p>
     * Ordenado por experiência total, mostrando os melhores jogadores do jogo.
     * </p>
     *
     * @return lista dos jogadores no ranking global
     */
    @GetMapping("/player/rankings")
    public ResponseEntity<List<PlayerService.RankingResponse>> getRankings() {
        return ResponseEntity.ok(playerService.getRankings());
    }

    // === TORRE DO CONHECIMENTO - QUESTS ===

    /**
     * Retorna todas as quests disponíveis na Torre do Conhecimento para um personagem.
     * <p>
     * Inclui informações de progresso se o personagem já aceitou alguma quest.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param characterId ID do personagem (obrigatório)
     * @return lista de quests disponíveis com progresso
     */
    @GetMapping("/tower/quests")
    public ResponseEntity<List<QuestDto>> getAvailableQuests(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Integer characterId) {

        // TODO: Validar que personagem pertence ao usuário
        List<QuestDto> quests = questService.getAvailableQuestsForCharacter(characterId);
        return ResponseEntity.ok(quests);
    }

    /**
     * Retorna as quests ativas (em progresso) de um personagem específico.
     *
     * @param userDetails detalhes do usuário autenticado
     * @param characterId ID do personagem (obrigatório)
     * @return lista de quests ativas
     */
    @GetMapping("/tower/quests/active")
    public ResponseEntity<List<QuestDto>> getActiveQuests(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Integer characterId) {

        // TODO: Validar que personagem pertence ao usuário
        List<QuestDto> quests = questService.getActiveQuests(characterId);
        return ResponseEntity.ok(quests);
    }

    /**
     * Aceita uma quest da Torre do Conhecimento para um personagem específico.
     * <p>
     * Cada personagem só pode ter uma quest ativa por vez.
     * </p>
     *
     * @param userDetails detalhes do usuário autenticado
     * @param request dados da quest a aceitar (questId e characterId)
     * @return informações da quest aceita
     */
    @PostMapping("/tower/quests/accept")
    public ResponseEntity<com.game.rpgbackend.dto.response.hub.AcceptQuestResponse> acceptQuest(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AcceptQuestRequest request) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        com.game.rpgbackend.dto.response.hub.AcceptQuestResponse response =
            questService.acceptQuest(userId, request.getQuestId(), request.getCharacterId());
        return ResponseEntity.ok(response);
    }

    /**
     * Abandona uma quest ativa de um personagem específico.
     *
     * @param userDetails detalhes do usuário autenticado
     * @param questId ID da quest a abandonar
     * @param characterId ID do personagem (obrigatório)
     * @return resposta de sucesso
     */
    @DeleteMapping("/tower/quests/{questId}/abandon")
    public ResponseEntity<String> abandonQuest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer questId,
            @RequestParam Integer characterId) {

        Integer userId = authenticationUtil.getUserIdFromUsername(userDetails.getUsername());
        questService.abandonQuest(userId, questId, characterId);
        return ResponseEntity.ok("Quest abandonada com sucesso");
    }

    // === MÉTODOS AUXILIARES ===

    /**
     * Converte entidade Book para DTO de resposta.
     *
     * @param book entidade de livro
     * @return DTO formatado para envio ao cliente
     */
    private BookDto mapToBookDto(Book book) {
        return new BookDto(book.getId(), book.getBookTitle(), book.getContent(), book.getType(), book.getDifficulty());
    }

    /**
     * Converte entidade Skill para DTO de resposta.
     *
     * @param skill entidade de habilidade
     * @return DTO formatado para envio ao cliente
     */
    private SkillDto mapToSkillDto(Skill skill) {
        return new SkillDto(skill.getId(), skill.getName(), skill.getDescription(), skill.getCost(), skill.getType(), skill.getEffect());
    }
}
