package com.game.rpgbackend.service.hub;

import com.game.rpgbackend.domain.Achievement;
import com.game.rpgbackend.domain.PlayerStats;
import com.game.rpgbackend.domain.User;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.AchievementRepository;
import com.game.rpgbackend.repository.BattleHistoryRepository;
import com.game.rpgbackend.repository.PlayerStatsRepository;
import com.game.rpgbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela gestão de estatísticas e progresso do jogador.
 * <p>
 * Gerencia todos os aspectos relacionados ao progresso e desempenho do jogador:
 * - Estatísticas gerais (nível, XP, ouro, batalhas, questões)
 * - Conquistas (achievements) desbloqueadas
 * - Histórico de batalhas realizadas
 * - Ranking global de jogadores
 * - Atualização de progresso após eventos do jogo
 * </p>
 * <p>
 * Este serviço é usado principalmente no Hub do jogo para exibir
 * informações sobre o desempenho e progresso do jogador.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerService {

    private final PlayerStatsRepository playerStatsRepository;
    private final AchievementRepository achievementRepository;
    private final BattleHistoryRepository battleHistoryRepository;
    private final UserRepository userRepository;

    /**
     * Busca as estatísticas do jogador ou cria um novo registro se não existir.
     * <p>
     * Se o jogador ainda não tiver estatísticas registradas (novo jogador),
     * um novo registro é criado automaticamente com valores iniciais:
     * - Nível: 1
     * - XP, Ouro, Batalhas: 0
     * - Questões corretas/erradas: 0
     * - Pontos de habilidade: 0
     * </p>
     * <p>
     * Este método garante que sempre haja estatísticas disponíveis para o jogador.
     * </p>
     *
     * @param userId identificador único do usuário
     * @return estatísticas do jogador (existentes ou recém-criadas)
     * @throws NotFoundException se o usuário não for encontrado no banco de dados
     */
    @Transactional
    public PlayerStats getPlayerStats(Integer userId) {
        return playerStatsRepository.findByUserId(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

                PlayerStats newStats = new PlayerStats();
                newStats.setUser(user);
                newStats.setLevel(1);
                newStats.setTotalXpEarned(0);
                newStats.setTotalGoldEarned(0);
                newStats.setBattlesWon(0);
                newStats.setBattlesLost(0);
                newStats.setQuestionsRight(0);
                newStats.setQuestionsWrong(0);
                newStats.setSkillPoints(0);
                return playerStatsRepository.save(newStats);
            });
    }

    /**
     * Busca todas as conquistas desbloqueadas por um jogador.
     * <p>
     * Conquistas são marcos especiais alcançados durante o jogo, como:
     * - Primeira vitória em batalha
     * - Alcançar nível X
     * - Responder N questões corretamente
     * - Completar missões específicas
     * </p>
     *
     * @param userId identificador único do usuário
     * @return lista de conquistas desbloqueadas pelo jogador
     */
    public List<Achievement> getAchievements(Integer userId) {
        return achievementRepository.findByUserId(userId);
    }

    /**
     * Retorna o histórico das últimas 10 batalhas do jogador.
     * <p>
     * O histórico inclui informações sobre cada batalha:
     * - Resultado (vitória, derrota)
     * - Nome do inimigo enfrentado
     * - Experiência ganha
     * - Data e hora da batalha
     * </p>
     * <p>
     * As batalhas são ordenadas por data decrescente (mais recentes primeiro).
     * </p>
     *
     * @param userId identificador único do usuário
     * @return lista das 10 últimas batalhas realizadas
     */
    public List<BattleHistoryResponse> getBattleHistory(Integer userId) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "date"));
        return battleHistoryRepository.findByUserId(userId).stream()
            .limit(10)
            .map(bh -> new BattleHistoryResponse(
                bh.getResult(),
                bh.getEnemyName(),
                bh.getXpEarned(),
                bh.getDate()
            ))
            .collect(Collectors.toList());
    }

    /**
     * Retorna o ranking global dos top 10 jogadores.
     * <p>
     * O ranking é ordenado por:
     * 1. Nível (decrescente) - jogadores de nível mais alto primeiro
     * 2. Total de XP (decrescente) - em caso de empate no nível
     * </p>
     * <p>
     * Exibe para cada jogador:
     * - Nome de usuário
     * - Nível atual
     * - Total de experiência acumulada
     * </p>
     *
     * @return lista dos 10 melhores jogadores do ranking global
     */
    public List<RankingResponse> getRankings() {
        Sort sort = Sort.by(
            Sort.Order.desc("level"),
            Sort.Order.desc("totalXpGanhos")
        );
        
        return playerStatsRepository.findAll(sort).stream()
            .limit(10)
            .map(stats -> new RankingResponse(
                stats.getUser().getUsername(),
                stats.getLevel(),
                stats.getTotalXpEarned()
            ))
            .collect(Collectors.toList());
    }

    /**
     * Atualiza as estatísticas de um jogador com novos valores.
     * <p>
     * Permite atualização parcial - apenas os campos não-nulos no objeto
     * de atualização serão modificados. Campos não fornecidos mantêm seus
     * valores atuais.
     * </p>
     * <p>
     * Campos atualizáveis:
     * - level: Nível do jogador
     * - totalXpGanhos: Total de XP acumulado
     * - totalOuroGanho: Total de ouro ganho
     * - battlesWon: Batalhas vencidas
     * - battlesLost: Batalhas perdidas
     * - questionsRight: Questões respondidas corretamente
     * - questionsWrong: Questões respondidas incorretamente
     * - skillPoints: Pontos de habilidade disponíveis
     * </p>
     *
     * @param userId identificador único do usuário
     * @param update objeto contendo os novos valores (campos opcionais)
     * @return estatísticas atualizadas e persistidas
     * @throws NotFoundException se as estatísticas do jogador não forem encontradas
     */
    @Transactional
    public PlayerStats updatePlayerStats(Integer userId, PlayerStatsUpdate update) {
        PlayerStats stats = playerStatsRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Estatísticas do jogador não encontradas"));

        if (update.getLevel() != null) {
            stats.setLevel(update.getLevel());
        }
        if (update.getTotalXpGanhos() != null) {
            stats.setTotalXpEarned(update.getTotalXpGanhos());
        }
        if (update.getTotalOuroGanho() != null) {
            stats.setTotalGoldEarned(update.getTotalOuroGanho());
        }
        if (update.getBattlesWon() != null) {
            stats.setBattlesWon(update.getBattlesWon());
        }
        if (update.getBattlesLost() != null) {
            stats.setBattlesLost(update.getBattlesLost());
        }
        if (update.getQuestionsRight() != null) {
            stats.setQuestionsRight(update.getQuestionsRight());
        }
        if (update.getQuestionsWrong() != null) {
            stats.setQuestionsWrong(update.getQuestionsWrong());
        }
        if (update.getSkillPoints() != null) {
            stats.setSkillPoints(update.getSkillPoints());
        }

        return playerStatsRepository.save(stats);
    }

    /**
     * DTO para atualização parcial de estatísticas do jogador.
     * <p>
     * Todos os campos são opcionais. Apenas os valores não-nulos
     * serão aplicados às estatísticas do jogador.
     * </p>
     */
    public static class PlayerStatsUpdate {
        private Integer level;
        private Integer totalXpGanhos;
        private Integer totalOuroGanho;
        private Integer battlesWon;
        private Integer battlesLost;
        private Integer questionsRight;
        private Integer questionsWrong;
        private Integer skillPoints;

        // Getters e Setters
        public Integer getLevel() { return level; }
        public void setLevel(Integer level) { this.level = level; }

        public Integer getTotalXpGanhos() { return totalXpGanhos; }
        public void setTotalXpGanhos(Integer totalXpGanhos) { this.totalXpGanhos = totalXpGanhos; }

        public Integer getTotalOuroGanho() { return totalOuroGanho; }
        public void setTotalOuroGanho(Integer totalOuroGanho) { this.totalOuroGanho = totalOuroGanho; }

        public Integer getBattlesWon() { return battlesWon; }
        public void setBattlesWon(Integer battlesWon) { this.battlesWon = battlesWon; }

        public Integer getBattlesLost() { return battlesLost; }
        public void setBattlesLost(Integer battlesLost) { this.battlesLost = battlesLost; }

        public Integer getQuestionsRight() { return questionsRight; }
        public void setQuestionsRight(Integer questionsRight) { this.questionsRight = questionsRight; }

        public Integer getQuestionsWrong() { return questionsWrong; }
        public void setQuestionsWrong(Integer questionsWrong) { this.questionsWrong = questionsWrong; }

        public Integer getSkillPoints() { return skillPoints; }
        public void setSkillPoints(Integer skillPoints) { this.skillPoints = skillPoints; }
    }

    /**
     * DTO de resposta para histórico de batalhas.
     * <p>
     * Contém informações resumidas de uma batalha realizada.
     * </p>
     */
    public static class BattleHistoryResponse {
        private String result;
        private String enemyName;
        private Integer experience;
        private java.time.LocalDateTime date;

        public BattleHistoryResponse(String result, String enemyName, Integer experience, java.time.LocalDateTime date) {
            this.result = result;
            this.enemyName = enemyName;
            this.experience = experience;
            this.date = date;
        }

        public String getResult() { return result; }
        public String getEnemyName() { return enemyName; }
        public Integer getExperience() { return experience; }
        public java.time.LocalDateTime getDate() { return date; }
    }

    /**
     * DTO de resposta para ranking de jogadores.
     * <p>
     * Contém informações de um jogador no ranking global.
     * </p>
     */
    public static class RankingResponse {
        private String nomeUsuario;
        private Integer level;
        private Integer totalXp;

        public RankingResponse(String nomeUsuario, Integer level, Integer totalXp) {
            this.nomeUsuario = nomeUsuario;
            this.level = level;
            this.totalXp = totalXp;
        }

        public String getNomeUsuario() { return nomeUsuario; }
        public Integer getLevel() { return level; }
        public Integer getTotalXp() { return totalXp; }
    }
}
