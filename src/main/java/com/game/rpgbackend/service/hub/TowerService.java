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
 * Serviço responsável pela gestão da Torre do Conhecimento.
 * <p>
 * A Torre do Conhecimento é o centro educacional do Hub onde jogadores
 * podem desbloquear habilidades especiais e acessar conteúdos educacionais
 * progressivos. Gerencia a progressão de aprendizado e o sistema de skills.
 * </p>
 * <p>
 * Funcionalidades principais:
 * - Listar habilidades disponíveis para desbloqueio
 * - Gerenciar pontos de habilidade do jogador
 * - Desbloquear novas habilidades usando pontos
 * - Exibir conteúdos educacionais por nível
 * - Rastrear progresso em conteúdos educacionais
 * - Validar requisitos de acesso a conteúdos
 * </p>
 * <p>
 * Sistema de progressão:
 * - Jogadores ganham pontos de habilidade ao subir de nível
 * - Pontos são usados para desbloquear habilidades permanentes
 * - Conteúdos educacionais têm nível mínimo requerido
 * - Progresso é rastreado por questões completadas
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TowerService {

    private final SkillRepository skillRepository;
    private final ContentRepository contentRepository;
    private final PlayerStatsRepository playerStatsRepository;

    /**
     * Retorna todas as habilidades disponíveis para desbloqueio na Torre.
     * <p>
     * Lista completa de skills que podem ser desbloqueadas usando pontos
     * de habilidade. Inclui informações de cada skill:
     * - Nome e descrição
     * - Custo em pontos de habilidade
     * - Tipo (passiva, ativa, etc.)
     * - Efeito no gameplay
     * </p>
     * <p>
     * O frontend deve verificar quais skills o jogador já possui
     * para exibir apenas as disponíveis para compra.
     * </p>
     *
     * @return lista de todas as habilidades do jogo
     */
    public List<Skill> getAvailableSkills() {
        return skillRepository.findAll();
    }

    /**
     * Busca conteúdos educacionais acessíveis para o nível do jogador.
     * <p>
     * Retorna todos os conteúdos educacionais que o jogador tem permissão
     * para acessar baseado em seu nível atual. Cada conteúdo inclui:
     * - Nome e descrição do tópico educacional
     * - Nível mínimo requerido
     * - Status de conclusão (completo/incompleto)
     * - Progresso percentual (baseado em questões respondidas)
     * </p>
     * <p>
     * Conteúdos são desbloqueados progressivamente conforme o jogador
     * sobe de nível, garantindo uma curva de aprendizado adequada.
     * </p>
     * <p>
     * TODO: O cálculo de progresso atual retorna valores fixos.
     * Implementar lógica real baseada em questões respondidas do conteúdo.
     * </p>
     *
     * @param playerLevel nível atual do jogador
     * @return lista de conteúdos acessíveis com status de progresso
     */
    public List<ContentWithProgress> getAvailableContent(Integer playerLevel) {
        List<Content> contents = contentRepository.findByMinLevelLessThanEqual(playerLevel);

        // TODO: Implementar cálculo de progresso real baseado nas questões respondidas
        return contents.stream()
            .map(content -> new ContentWithProgress(
                content.getId(),
                content.getContentName(),
                content.getDescription(),
                content.getMinLevel(),
                false, // completed
                0      // progress
            ))
            .collect(Collectors.toList());
    }

    /**
     * Busca informações detalhadas de um conteúdo educacional específico.
     * <p>
     * Retorna dados completos sobre um tópico educacional incluindo:
     * - Nome e descrição do conteúdo
     * - Nível mínimo necessário para acesso
     * - Status de conclusão do jogador
     * - Progresso atual (percentual de questões respondidas)
     * </p>
     * <p>
     * Usado quando o jogador seleciona um conteúdo específico para estudar
     * ou revisar seu progresso em um tópico.
     * </p>
     * <p>
     * TODO: O cálculo de progresso atual retorna valores fixos (false, 0).
     * Implementar lógica real consultando as questões respondidas pelo
     * jogador relacionadas a este conteúdo específico.
     * </p>
     *
     * @param contentId identificador único do conteúdo educacional
     * @return conteúdo com informações de progresso
     * @throws NotFoundException se o conteúdo não for encontrado
     */
    public ContentWithProgress getContentById(Integer contentId) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new NotFoundException("Conteúdo não encontrado"));

        // TODO: Implementar cálculo de progresso real
        return new ContentWithProgress(
            content.getId(),
            content.getContentName(),
            content.getDescription(),
            content.getMinLevel(),
            false,
            0
        );
    }

    /**
     * Verifica se o jogador atende aos requisitos para acessar um conteúdo.
     * <p>
     * Valida se o nível atual do jogador é suficiente para desbloquear
     * e estudar o conteúdo educacional especificado.
     * </p>
     * <p>
     * Esta verificação é importante para:
     * - Prevenir acesso a conteúdo muito avançado
     * - Manter progressão de dificuldade adequada
     * - Garantir que o jogador tenha conhecimento prévio necessário
     * </p>
     * <p>
     * Pode ser expandida no futuro para incluir outros requisitos como:
     * - Conteúdos pré-requisitos completados
     * - Quests específicas finalizadas
     * - Conquistas desbloqueadas
     * </p>
     *
     * @param contentId identificador do conteúdo a ser verificado
     * @param playerLevel nível atual do jogador
     * @return true se o jogador pode acessar o conteúdo, false caso contrário
     * @throws NotFoundException se o conteúdo não for encontrado
     */
    public boolean checkContentRequirements(Integer contentId, Integer playerLevel) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new NotFoundException("Conteúdo não encontrado"));

        return playerLevel >= content.getMinLevel();
    }

    /**
     * Processa o desbloqueio de uma habilidade usando pontos de habilidade.
     * <p>
     * Executa a transação completa de compra de skill:
     * 1. Valida existência do jogador e da habilidade
     * 2. Verifica se o jogador possui pontos suficientes
     * 3. Verifica se a habilidade já não foi desbloqueada
     * 4. Adiciona a skill às habilidades desbloqueadas do jogador
     * 5. Deduz o custo em pontos de habilidade
     * 6. Persiste as alterações no banco de dados
     * </p>
     * <p>
     * Pontos de habilidade são ganhos ao subir de nível e são um
     * recurso limitado que deve ser gasto estrategicamente.
     * </p>
     * <p>
     * Operação transacional: Em caso de erro, todas as alterações
     * são revertidas automaticamente (rollback).
     * </p>
     *
     * @param userId identificador único do usuário
     * @param skillId identificador da habilidade a ser desbloqueada
     * @return habilidade desbloqueada com todas as suas informações
     * @throws NotFoundException se jogador ou habilidade não forem encontrados
     * @throws BadRequestException se pontos forem insuficientes ou habilidade já possuída
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

    /**
     * DTO de resposta para conteúdo educacional com informações de progresso.
     * <p>
     * Combina dados do conteúdo educacional com o progresso individual
     * do jogador naquele tópico, incluindo:
     * - Informações básicas do conteúdo (nome, descrição, nível)
     * - Status de conclusão (completo/incompleto)
     * - Progresso percentual (0-100)
     * </p>
     */
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
