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
 * Controller responsável pelas operações de diálogos educacionais.
 */
@RestController
@RequestMapping("/api/dialogs")
@RequiredArgsConstructor
public class DialogController {

    private final DialogService dialogService;

    /**
     * Busca diálogos por nível do personagem e conteúdo.
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
     * Busca um diálogo específico por ID.
     */
    @GetMapping("/{dialogId}")
    public ResponseEntity<DialogResponseDto> getDialogById(@PathVariable Integer dialogId) {
        DialogService.DialogResponse dialog = dialogService.getDialogById(dialogId);
        DialogResponseDto dto = mapToDto(dialog);
        return ResponseEntity.ok(dto);
    }

    /**
     * Calcula a proporção de inglês/português para um diálogo.
     */
    @GetMapping("/language-ratio")
    public ResponseEntity<LanguageRatioResponse> calculateLanguageRatio(
            @RequestParam Integer characterLevel,
            @RequestParam Integer dialogMinLevel) {

        double ratio = dialogService.calculateLanguageRatio(characterLevel, dialogMinLevel);
        LanguageRatioResponse response = new LanguageRatioResponse(ratio);
        return ResponseEntity.ok(response);
    }

    private DialogResponseDto mapToDto(DialogService.DialogResponse response) {
        DialogResponseDto dto = new DialogResponseDto();
        dto.setId(response.getId());
        dto.setPortugues(response.getPortugues());
        dto.setIngles(response.getIngles());
        dto.setKeywords(response.getKeywords().stream().map(this::mapKeywordToDto).collect(Collectors.toList()));
        return dto;
    }

    private KeywordResponseDto mapKeywordToDto(DialogService.KeywordResponse response) {
        KeywordResponseDto dto = new KeywordResponseDto();
        dto.setPalavra(response.getPalavra());
        dto.setTraducao(response.getTraducao());
        dto.setDestaque(response.getDestaque());
        return dto;
    }
}
