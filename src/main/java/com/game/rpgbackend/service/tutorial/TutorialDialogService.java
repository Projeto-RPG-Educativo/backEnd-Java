package com.game.rpgbackend.service.tutorial;

import com.game.rpgbackend.domain.Dialogue;
import com.game.rpgbackend.repository.DialogueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelos diálogos do tutorial inicial do jogo.
 * <p>
 * Gerencia os diálogos introdutórios que guiam novos jogadores através
 * dos primeiros passos do sistema RPG, explicando mecânicas básicas como:
 * - Sistema de batalha
 * - Questões educacionais
 * - Seleção de classe
 * - Controles e interface
 * </p>
 * <p>
 * Os diálogos de tutorial são apresentados por NPCs específicos e seguem
 * uma sequência definida para garantir uma experiência de onboarding consistente.
 * </p>
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
     * Busca um diálogo de tutorial específico por identificador.
     * <p>
     * Retorna o diálogo completo incluindo conteúdo, resposta esperada
     * e informações do NPC que apresenta o tutorial.
     * </p>
     *
     * @param id identificador único do diálogo de tutorial
     * @return diálogo de tutorial encontrado
     * @throws RuntimeException se o diálogo não for encontrado no banco de dados
     */
    public Dialogue findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TutorialDialog not found with id: " + id));
    }
}