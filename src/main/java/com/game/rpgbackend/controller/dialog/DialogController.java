package com.game.rpgbackend.controller.dialog;

import com.game.rpgbackend.dto.response.dialog.DialogResponseDto;
import com.game.rpgbackend.dto.response.dialog.KeywordResponseDto;
import com.game.rpgbackend.dto.response.dialog.LanguageRatioResponse;
import com.game.rpgbackend.service.dialog.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST responsável pelas operações de diálogos educacionais.
 * <p>
 * Gerencia os diálogos que ensinam inglês ao jogador, incluindo
 * a proporção de inglês/português baseada no nível do personagem,
 * palavras-chave destacadas e contextos educacionais.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/dialogs")
@RequiredArgsConstructor
public class DialogController {

    private final DialogService dialogService;

    /**
     * Busca diálogos filtrados por nível do personagem e conteúdo educacional.
     * <p>
     * Os diálogos são adaptados ao nível do jogador, com proporção
     * adequada de inglês/português e palavras-chave destacadas.
     * </p>
     *
     * @param contentId identificador do conteúdo educacional
     * @param characterLevel nível atual do personagem para ajuste de dificuldade
     * @return lista de diálogos adaptados ao nível do personagem
     */
    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<DialogResponseDto>> getDialogsByLevel(
            @PathVariable Integer contentId,
            @RequestParam Integer characterLevel) {

        List<DialogService.DialogResponse> dialogs = dialogService.getDialogsByLevel(characterLevel, contentId);
        List<DialogResponseDto> dtoList = dialogs.stream().map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Busca um diálogo educacional específico por seu identificador.
     * <p>
     * Retorna o diálogo completo com texto, palavras-chave e NPC associado.
     * </p>
     *
     * @param dialogId identificador único do diálogo
     * @return DTO completo do diálogo ou 404 se não encontrado
     */
    @GetMapping("/{dialogId}")
    public ResponseEntity<DialogResponseDto> getDialogById(@PathVariable Integer dialogId) {
        DialogService.DialogResponse dialog = dialogService.getDialogById(dialogId);
        DialogResponseDto dto = mapToDto(dialog);
        return ResponseEntity.ok(dto);
    }

    /**
     * Calcula a proporção de inglês/português ideal para um diálogo.
     * <p>
     * A proporção é calculada baseada no nível do personagem:
     * - Níveis baixos: mais português
     * - Níveis altos: mais inglês
     * </p>
     *
     * @param characterLevel nível atual do personagem
     * @return proporção calculada de inglês vs português
     */
    @GetMapping("/language-ratio")
    public ResponseEntity<LanguageRatioResponse> calculateLanguageRatio(
            @RequestParam Integer characterLevel,
            @RequestParam Integer dialogMinLevel) {

        double ratio = dialogService.calculateLanguageRatio(characterLevel, dialogMinLevel);
        LanguageRatioResponse response = new LanguageRatioResponse(ratio);
        return ResponseEntity.ok(response);
    }

    /**
     * Converte a resposta do serviço de diálogo para DTO de resposta.
     *
     * @param response objeto de resposta do serviço de diálogo
     * @return DTO formatado para envio ao cliente
     */
    private DialogResponseDto mapToDto(DialogService.DialogResponse response) {
        DialogResponseDto dto = new DialogResponseDto();
        dto.setId(response.getId());
        dto.setPortugues(response.getPortugues());
        dto.setIngles(response.getIngles());
        dto.setKeywords(response.getKeywords().stream().map(this::mapKeywordToDto).collect(Collectors.toList()));
        return dto;
    }

    /**
     * Converte palavra-chave do serviço para DTO de resposta.
     *
     * @param response objeto de resposta com dados da palavra-chave
     * @return DTO da palavra-chave formatado para o cliente
     */
    private KeywordResponseDto mapKeywordToDto(DialogService.KeywordResponse response) {
        KeywordResponseDto dto = new KeywordResponseDto();
        dto.setPalavra(response.getPalavra());
        dto.setTraducao(response.getTraducao());
        dto.setDestaque(response.getDestaque());
        return dto;
    }
}
