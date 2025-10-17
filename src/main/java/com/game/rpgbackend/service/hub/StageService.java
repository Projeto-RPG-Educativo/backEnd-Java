package com.game.rpgbackend.service.hub;

import com.game.rpgbackend.domain.Dialogue;
import com.game.rpgbackend.domain.NPC;
import com.game.rpgbackend.repository.DialogueRepository;
import com.game.rpgbackend.repository.NPCRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pelo Palco da Retórica (NPCs e Diálogos).
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StageService {

    private final NPCRepository npcRepository;
    private final DialogueRepository dialogueRepository;

    /**
     * Busca todos os NPCs disponíveis.
     */
    public List<NPC> getNPCs() {
        return npcRepository.findAll();
    }

    /**
     * Busca os diálogos de um NPC específico.
     */
    public List<Dialogue> getNPCDialogues(Integer npcId) {
        return dialogueRepository.findByNpcId(npcId);
    }

    /**
     * Busca NPCs por tipo.
     */
    public List<NPC> getNPCsByType(String type) {
        return npcRepository.findByType(type);
    }

    /**
     * Busca NPCs por localização.
     */
    public List<NPC> getNPCsByLocation(String location) {
        return npcRepository.findByLocation(location);
    }
}

