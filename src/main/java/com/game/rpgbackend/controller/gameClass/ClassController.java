package com.game.rpgbackend.controller.gameClass;

import com.game.rpgbackend.domain.GameClass;
import com.game.rpgbackend.dto.response.GameClassDTO;
import com.game.rpgbackend.service.classe.ClassService;
import com.game.rpgbackend.util.GameClassMapper;
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
    private final GameClassMapper gameClassMapper;

    /**
     * Busca todas as classes disponíveis.
     */
    @GetMapping
    public ResponseEntity<List<GameClassDTO>> getAllClasses() {
        List<GameClass> classes = classService.getAllClasses();
        return ResponseEntity.ok(gameClassMapper.toDTOList(classes));
    }

    /**
     * Busca uma classe específica por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameClassDTO> getClassById(@PathVariable Integer id) {
        GameClass clazz = classService.getClassById(id);
        return ResponseEntity.ok(gameClassMapper.toDTO(clazz));
    }

    /**
     * Busca uma classe por nome.
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<GameClassDTO> getClassByName(@PathVariable String name) {
        GameClass clazz = classService.getClassByName(name);
        return ResponseEntity.ok(gameClassMapper.toDTO(clazz));
    }
}
