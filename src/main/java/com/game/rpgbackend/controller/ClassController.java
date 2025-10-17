package com.game.rpgbackend.controller;

import com.game.rpgbackend.domain.GameClass;
import com.game.rpgbackend.service.classe.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelas operações de classes de personagem.
 */
@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    /**
     * Busca todas as classes disponíveis.
     */
    @GetMapping
    public ResponseEntity<List<GameClass>> getAllClasses() {
        List<GameClass> classes = classService.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    /**
     * Busca uma classe específica por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameClass> getClassById(@PathVariable Integer id) {
        GameClass clazz = classService.getClassById(id);
        return ResponseEntity.ok(clazz);
    }

    /**
     * Busca uma classe por nome.
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<GameClass> getClassByName(@PathVariable String name) {
        GameClass clazz = classService.getClassByName(name);
        return ResponseEntity.ok(clazz);
    }
}
