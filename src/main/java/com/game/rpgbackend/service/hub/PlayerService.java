package com.game.rpgbackend.service.hub;

import com.game.rpgbackend.domain.Achievement;
import com.game.rpgbackend.domain.BattleHistory;
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
 * Serviço responsável pelas estatísticas e progresso do jogador.
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
     * Busca ou cria as estatísticas do jogador.
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
                newStats.setTotalXpGanhos(0);
                newStats.setTotalOuroGanho(0);
                newStats.setBattlesWon(0);
                newStats.setBattlesLost(0);
                newStats.setQuestionsRight(0);
                newStats.setQuestionsWrong(0);
                newStats.setSkillPoints(0);
                return playerStatsRepository.save(newStats);
            });
    }

    /**
     * Busca as conquistas de um jogador.
     */
    public List<Achievement> getAchievements(Integer userId) {
        return achievementRepository.findByUserId(userId);
    }

    /**
     * Busca o histórico de batalhas de um jogador (últimas 10).
     */
    public List<BattleHistoryResponse> getBattleHistory(Integer userId) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "date"));
        return battleHistoryRepository.findByUserId(userId).stream()
            .limit(10)
            .map(bh -> new BattleHistoryResponse(
                bh.getResult(),
                bh.getEnemyName(),
                bh.getExperience(),
                bh.getDate()
            ))
            .collect(Collectors.toList());
    }

    /**
     * Busca o ranking dos jogadores (top 10).
     */
    public List<RankingResponse> getRankings() {
        Sort sort = Sort.by(
            Sort.Order.desc("level"),
            Sort.Order.desc("totalXpGanhos")
        );
        
        return playerStatsRepository.findAll(sort).stream()
            .limit(10)
            .map(stats -> new RankingResponse(
                stats.getUser().getNomeUsuario(),
                stats.getLevel(),
                stats.getTotalXpGanhos()
            ))
            .collect(Collectors.toList());
    }

    /**
     * Atualiza as estatísticas do jogador.
     */
    @Transactional
    public PlayerStats updatePlayerStats(Integer userId, PlayerStatsUpdate update) {
        PlayerStats stats = playerStatsRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Estatísticas do jogador não encontradas"));

        if (update.getLevel() != null) {
            stats.setLevel(update.getLevel());
        }
        if (update.getTotalXpGanhos() != null) {
            stats.setTotalXpGanhos(update.getTotalXpGanhos());
        }
        if (update.getTotalOuroGanho() != null) {
            stats.setTotalOuroGanho(update.getTotalOuroGanho());
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

    // Classes internas
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
