package com.game.rpgbackend.service.classe;

import com.game.rpgbackend.domain.GameClass;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela gestão de classes de personagem.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassService {

    private final ClassRepository classRepository;

    /**
     * Retorna todas as classes disponíveis com suas características.
     */
    public List<GameClass> getAllClasses() {
        return classRepository.findAll();
    }

    /**
     * Busca uma classe específica por ID.
     */
    public GameClass getClassById(Integer id) {
        return classRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Classe não encontrada"));
    }

    /**
     * Busca uma classe por nome.
     */
    public GameClass getClassByName(String name) {
        return classRepository.findByName(name)
            .orElseThrow(() -> new NotFoundException("Classe não encontrada"));
    }
}
