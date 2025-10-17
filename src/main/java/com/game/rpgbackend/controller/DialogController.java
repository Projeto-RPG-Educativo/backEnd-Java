package com.game.rpgbackend.controller;

import com.game.rpgbackend.service.dialog.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<DialogService.DialogResponse>> getDialogsByLevel(
            @PathVariable Integer contentId,
            @RequestParam Integer characterLevel) {

        List<DialogService.DialogResponse> dialogs = dialogService.getDialogsByLevel(characterLevel, contentId);
        return ResponseEntity.ok(dialogs);
    }

    /**
     * Busca um diálogo específico por ID.
     */
    @GetMapping("/{dialogId}")
    public ResponseEntity<DialogService.DialogResponse> getDialogById(@PathVariable Integer dialogId) {
        DialogService.DialogResponse dialog = dialogService.getDialogById(dialogId);
        return ResponseEntity.ok(dialog);
    }

    /**
     * Calcula a proporção de inglês/português para um diálogo.
     */
    @GetMapping("/language-ratio")
    public ResponseEntity<Map<String, Double>> calculateLanguageRatio(
            @RequestParam Integer characterLevel,
            @RequestParam Integer dialogMinLevel) {

        double ratio = dialogService.calculateLanguageRatio(characterLevel, dialogMinLevel);
        return ResponseEntity.ok(Map.of("ratio", ratio));
    }
}

