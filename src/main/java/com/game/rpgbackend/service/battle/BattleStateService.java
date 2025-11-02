package com.game.rpgbackend.service.battle;

import com.game.rpgbackend.dto.response.battle.BattleStateResponse;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço responsável por gerenciar o estado das batalhas ativas em memória.
 * <p>
 * Mantém uma cache em memória (ConcurrentHashMap) com o estado completo
 * de todas as batalhas em andamento, indexadas por ID do usuário.
 * Isso permite acesso rápido ao estado da batalha sem necessidade de
 * persistência em banco de dados a cada ação.
 * </p>
 * <p>
 * Thread-safe: Utiliza ConcurrentHashMap para suportar acesso concorrente
 * de múltiplos usuários simultaneamente.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
public class BattleStateService {

    /**
     * Mapa thread-safe que armazena as batalhas ativas indexadas por ID do usuário.
     * Cada usuário pode ter no máximo uma batalha ativa por vez.
     */
    private final Map<Integer, BattleStateResponse> activeBattles = new ConcurrentHashMap<>();

    /**
     * Busca a batalha ativa de um usuário específico.
     * <p>
     * Retorna o estado completo da batalha incluindo HP, energia,
     * questão atual, efeitos ativos e informações de turno.
     * </p>
     *
     * @param userId identificador único do usuário
     * @return estado completo da batalha ou null se não houver batalha ativa
     */
    public BattleStateResponse getActiveBattle(Integer userId) {
        return activeBattles.get(userId);
    }

    /**
     * Salva ou atualiza o estado da batalha de um usuário na memória.
     * <p>
     * Se já existir uma batalha ativa para o usuário, ela será sobrescrita.
     * Este método é chamado após cada ação de batalha para manter o estado atualizado.
     * </p>
     *
     * @param userId identificador único do usuário
     * @param battleState objeto completo com o estado atual da batalha
     */
    public void setActiveBattle(Integer userId, BattleStateResponse battleState) {
        activeBattles.put(userId, battleState);
    }

    /**
     * Remove a batalha ativa de um usuário da memória.
     * <p>
     * Geralmente chamado quando a batalha termina (vitória, derrota ou fuga).
     * Libera recursos de memória e permite que o usuário inicie uma nova batalha.
     * </p>
     *
     * @param userId identificador único do usuário
     */
    public void removeActiveBattle(Integer userId) {
        activeBattles.remove(userId);
    }

    /**
     * Verifica se um usuário possui uma batalha em andamento.
     * <p>
     * Útil para validar se o usuário pode iniciar uma nova batalha
     * ou se precisa finalizar a atual primeiro.
     * </p>
     *
     * @param userId identificador único do usuário
     * @return true se houver batalha ativa, false caso contrário
     */
    public boolean hasActiveBattle(Integer userId) {
        return activeBattles.containsKey(userId);
    }

    /**
     * Remove todas as batalhas ativas da memória.
     * <p>
     * Método utilitário usado principalmente para:
     * - Testes automatizados
     * - Manutenção do servidor
     * - Reset em caso de problemas críticos
     * </p>
     * <p>
     * ATENÇÃO: Este método fará com que todos os jogadores percam
     * o estado atual de suas batalhas.
     * </p>
     */
    public void clearAllBattles() {
        activeBattles.clear();
    }
}

