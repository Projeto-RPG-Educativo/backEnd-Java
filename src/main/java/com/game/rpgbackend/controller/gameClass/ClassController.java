package com.game.rpgbackend.controller.gameClass;

import com.game.rpgbackend.domain.GameClass;
import com.game.rpgbackend.dto.response.gameClass.GameClassDTO;
import com.game.rpgbackend.service.classe.ClassService;
import com.game.rpgbackend.util.GameClassMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST responsável pelas operações de classes de personagem.
 * <p>
 * Fornece endpoints para consultar as classes jogáveis disponíveis,
 * incluindo seus atributos base, habilidades especiais e características.
 * Classes disponíveis: Lutador, Mago, Bardo, Ladino, Paladino e Tank.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;
    private final GameClassMapper gameClassMapper;

    /**
     * Retorna todas as classes de personagem disponíveis no jogo.
     * <p>
     * Inclui informações completas de cada classe: atributos base,
     * HP, energia, dano e descrição das habilidades especiais.
     * </p>
     *
     * @return lista com todas as classes disponíveis
     */
    @GetMapping
    public ResponseEntity<List<GameClassDTO>> getAllClasses() {
        List<GameClass> classes = classService.getAllClasses();
        return ResponseEntity.ok(gameClassMapper.toDTOList(classes));
    }

    /**
     * Busca uma classe específica por seu identificador único.
     *
     * @param id identificador único da classe
     * @return DTO completo da classe ou 404 se não encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameClassDTO> getClassById(@PathVariable Integer id) {
        GameClass clazz = classService.getClassById(id);
        return ResponseEntity.ok(gameClassMapper.toDTO(clazz));
    }

    /**
     * Busca uma classe pelo seu nome.
     * <p>
     * Nomes aceitos: lutador, mago, bardo, ladino, paladino, tank.
     * </p>
     *
     * @param name nome da classe (case-insensitive)
     * @return DTO completo da classe ou 404 se não encontrada
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<GameClassDTO> getClassByName(@PathVariable String name) {
        GameClass clazz = classService.getClassByName(name);
        return ResponseEntity.ok(gameClassMapper.toDTO(clazz));
    }
}
