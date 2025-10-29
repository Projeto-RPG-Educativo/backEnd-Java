package com.game.rpgbackend.service.tutorial;

import com.game.rpgbackend.domain.Dialogue;
import com.game.rpgbackend.repository.DialogueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serviço para gerenciar operações relacionadas a TutorialDialog.
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
public class TutorialDialogService {

    @Autowired
    private DialogueRepository repository;

    /**
     * Busca um TutorialDialog pelo ID.
     *
     * @param id o ID do TutorialDialog
     * @return o TutorialDialog encontrado
     * @throws RuntimeException se o TutorialDialog não for encontrado
     */
    public Dialogue findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TutorialDialog not found with id: " + id));
    }
}