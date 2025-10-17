package com.game.rpgbackend.service.battle;

import com.game.rpgbackend.dto.response.BattleStateResponse;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço responsável por gerenciar o estado das batalhas ativas em memória.
 * Mantém o estado das batalhas para cada usuário durante o combate.
 */
@Service
public class BattleStateService {

    // Armazena as batalhas ativas por userId
    private final Map<Integer, BattleStateResponse> activeBattles = new ConcurrentHashMap<>();

    /**
     * Busca a batalha ativa para um determinado usuário.
     * @param userId ID do usuário.
     * @return O estado da batalha ou null se não houver.
     */
    public BattleStateResponse getActiveBattle(Integer userId) {
        return activeBattles.get(userId);
    }

    /**
     * Salva ou atualiza o estado da batalha para um usuário.
     * @param userId ID do usuário.
     * @param battleState O objeto completo do estado da batalha.
     */
    public void setActiveBattle(Integer userId, BattleStateResponse battleState) {
        activeBattles.put(userId, battleState);
    }

    /**
     * Remove a batalha ativa de um usuário, geralmente ao final do combate.
     * @param userId ID do usuário.
     */
    public void removeActiveBattle(Integer userId) {
        activeBattles.remove(userId);
    }

    /**
     * Verifica se um usuário possui uma batalha ativa.
     * @param userId ID do usuário.
     * @return true se houver uma batalha ativa, false caso contrário.
     */
    public boolean hasActiveBattle(Integer userId) {
        return activeBattles.containsKey(userId);
    }

    /**
     * Limpa todas as batalhas ativas (útil para manutenção ou testes).
     */
    public void clearAllBattles() {
        activeBattles.clear();
    }
}

