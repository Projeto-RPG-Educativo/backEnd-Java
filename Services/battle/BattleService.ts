import api from '../api';
import type {
  BattleStartRequest,
  BattleState,
  BattleActionRequest,
  AnswerSubmitRequest,
  AnswerSubmitResponse,
  BattleSaveProgressRequest,
  BattleSaveProgressResponse,
} from '../../types/Battle';

/**
 * Inicia uma nova batalha
 */
export const startBattle = async (data: BattleStartRequest): Promise<BattleState> => {
  const response = await api.post<BattleState>('/batalha/iniciar', data);
  return response.data;
};

/**
 * Realiza uma ação de batalha (ataque, defesa, habilidade, item)
 */
export const performBattleAction = async (data: BattleActionRequest): Promise<BattleState> => {
  const response = await api.post<BattleState>('/batalha/action', data);
  return response.data;
};

/**
 * Submete a resposta de uma pergunta durante a batalha
 */
export const submitAnswer = async (
  data: AnswerSubmitRequest
): Promise<AnswerSubmitResponse> => {
  const response = await api.post<AnswerSubmitResponse>('/batalha/responder', data);
  return response.data;
};

/**
 * Salva o progresso da batalha
 */
export const saveBattleProgress = async (
  data: BattleSaveProgressRequest
): Promise<BattleSaveProgressResponse> => {
  const response = await api.post<BattleSaveProgressResponse>(
    '/batalha/salvar-progresso',
    data
  );
  return response.data;
};

export default {
  startBattle,
  performBattleAction,
  submitAnswer,
  saveBattleProgress,
};
