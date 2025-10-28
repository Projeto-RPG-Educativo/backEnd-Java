package com.game.rpgbackend.controller.tutorial;

import com.game.rpgbackend.domain.Dialogue;
import com.game.rpgbackend.service.tutorial.TutorialDialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operações relacionadas a TutorialDialog.
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/tutorial-dialog")
public class TutorialDialogController {

    @Autowired
    private TutorialDialogService service;

    /**
     * Busca um TutorialDialog pelo ID.
     *
     * @param id o ID do TutorialDialog
     * @return ResponseEntity contendo o TutorialDialog
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dialogue> getById(@PathVariable Integer id) {
        Dialogue tutorialDialog = service.findById(id);
        return ResponseEntity.ok(tutorialDialog);
    }
}
