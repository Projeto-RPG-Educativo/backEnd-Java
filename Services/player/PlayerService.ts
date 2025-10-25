import api from '../api';
import type {
  PlayerStats,
  PlayerAchievements,
  BattleHistory,
  Rankings,
  UpdatePlayerStatsRequest,
} from '../../types/Player';

/**
 * Busca as estatísticas do jogador
 */
export const getPlayerStats = async (): Promise<PlayerStats> => {
  const response = await api.get<PlayerStats>('/player/stats');
  return response.data;
};

/**
 * Busca as conquistas do jogador
 */
export const getPlayerAchievements = async (): Promise<PlayerAchievements> => {
  const response = await api.get<PlayerAchievements>('/player/achievements');
  return response.data;
};

/**
 * Busca o histórico de batalhas do jogador
 */
export const getBattleHistory = async (): Promise<BattleHistory> => {
  const response = await api.get<BattleHistory>('/player/battle-history');
  return response.data;
};

/**
 * Busca o ranking global de jogadores
 */
export const getRankings = async (): Promise<Rankings> => {
  const response = await api.get<Rankings>('/player/rankings');
  return response.data;
};

/**
 * Atualiza as estatísticas do jogador
 */
// export const updatePlayerStats = async (
//   data: UpdatePlayerStatsRequest
// ): Promise<PlayerStats> => {
//   const response = await api.put<PlayerStats>('/player/stats', data);
//   return response.data;
// }; 

// sem rotas no backend

export default {
  getPlayerStats,
  getPlayerAchievements,
  getBattleHistory,
  getRankings,
};
