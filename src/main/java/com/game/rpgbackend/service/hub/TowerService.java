package com.game.rpgbackend.service.hub;

import com.game.rpgbackend.domain.Content;
import com.game.rpgbackend.domain.PlayerStats;
import com.game.rpgbackend.domain.Skill;
import com.game.rpgbackend.exception.BadRequestException;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.ContentRepository;
import com.game.rpgbackend.repository.PlayerStatsRepository;
import com.game.rpgbackend.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela Torre do Conhecimento (Skills e Conteúdo).
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TowerService {

    private final SkillRepository skillRepository;
    private final ContentRepository contentRepository;
    private final PlayerStatsRepository playerStatsRepository;

    /**
     * Retorna todas as habilidades disponíveis.
     */
    public List<Skill> getAvailableSkills() {
        return skillRepository.findAll();
    }

    /**
     * Busca conteúdos disponíveis para o nível do jogador.
     */
    public List<ContentWithProgress> getAvailableContent(Integer playerLevel) {
        List<Content> contents = contentRepository.findByLevelMinimoLessThanEqual(playerLevel);

        // TODO: Implementar cálculo de progresso real baseado nas questões respondidas
        return contents.stream()
            .map(content -> new ContentWithProgress(
                content.getId(),
                content.getNome(),
                content.getDescricao(),
                content.getLevelMinimo(),
                false, // completed
                0      // progress
            ))
            .collect(Collectors.toList());
    }

    /**
     * Busca um conteúdo específico por ID.
     */
    public ContentWithProgress getContentById(Integer contentId) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new NotFoundException("Conteúdo não encontrado"));

        // TODO: Implementar cálculo de progresso real
        return new ContentWithProgress(
            content.getId(),
            content.getNome(),
            content.getDescricao(),
            content.getLevelMinimo(),
            false,
            0
        );
    }

    /**
     * Verifica se o jogador atende aos requisitos de um conteúdo.
     */
    public boolean checkContentRequirements(Integer contentId, Integer playerLevel) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new NotFoundException("Conteúdo não encontrado"));

        return playerLevel >= content.getLevelMinimo();
    }

    /**
     * Processa a compra de uma habilidade.
     */
    @Transactional
    public Skill purchaseSkill(Integer userId, Integer skillId) {
        PlayerStats player = playerStatsRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Jogador não encontrado"));

        Skill skill = skillRepository.findById(skillId)
            .orElseThrow(() -> new NotFoundException("Habilidade não encontrada"));

        // Verifica se o jogador tem pontos suficientes
        if (player.getSkillPoints() < skill.getCost()) {
            throw new BadRequestException("Pontos de habilidade insuficientes");
        }

        // Verifica se já possui a habilidade
        if (player.getUnlockedSkills() != null && player.getUnlockedSkills().contains(skill)) {
            throw new BadRequestException("Você já possui esta habilidade");
        }

        // Adiciona a skill e deduz os pontos
        if (player.getUnlockedSkills() == null) {
            player.setUnlockedSkills(new java.util.ArrayList<>());
        }
        player.getUnlockedSkills().add(skill);
        player.setSkillPoints(player.getSkillPoints() - skill.getCost());

        playerStatsRepository.save(player);

        return skill;
    }

    // Classe interna para conteúdo com progresso
    public static class ContentWithProgress {
        private Integer id;
        private String nome;
        private String descricao;
        private Integer levelMinimo;
        private boolean completed;
        private int progress;

        public ContentWithProgress(Integer id, String nome, String descricao, Integer levelMinimo, boolean completed, int progress) {
            this.id = id;
            this.nome = nome;
            this.descricao = descricao;
            this.levelMinimo = levelMinimo;
            this.completed = completed;
            this.progress = progress;
        }

        // Getters
        public Integer getId() { return id; }
        public String getNome() { return nome; }
        public String getDescricao() { return descricao; }
        public Integer getLevelMinimo() { return levelMinimo; }
        public boolean isCompleted() { return completed; }
        public int getProgress() { return progress; }
    }
}

