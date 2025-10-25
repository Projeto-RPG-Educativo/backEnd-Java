import api from '../api';
import type {
  HubStats,
  SkillsResponse,
  PurchaseSkillRequest,
  PurchaseSkillResponse,
  BooksResponse,
  BookDetails,
  ProfessorsResponse,
  ProfessorDialoguesResponse,
  StoresResponse,
  PurchaseItemRequest,
  PurchaseItemResponse,
} from '../../types/Hub';

import type { 
    PlayerStats, 
    PlayerAchievements, 
    BattleHistory, 
    Rankings } 
    from '../../types/Player';

// ==================== Player Stats & Progress ====================

/**
 * Busca as estatísticas do jogador no hub
 */
export const getHubStats = async (): Promise<PlayerStats> => {
  const response = await api.get<PlayerStats>('/hub/stats');
  return response.data;
};

/**
 * Busca as conquistas do jogador
 */
export const getAchievements = async (): Promise<PlayerAchievements> => {
  const response = await api.get<PlayerAchievements>('/hub/achievements');
  return response.data;
};

/**
 * Busca o histórico de batalhas
 */
export const getBattleHistoryFromHub = async (): Promise<BattleHistory> => {
  const response = await api.get<BattleHistory>('/hub/history');
  return response.data;
};

/**
 * Busca o ranking de jogadores
 */
export const getRankingsFromHub = async (): Promise<Rankings> => {
  const response = await api.get<Rankings>('/hub/rankings');
  return response.data;
};

/**
 * Atualiza as estatísticas do jogador
 */
export const updateHubStats = async (data: Partial<HubStats>): Promise<PlayerStats> => {
  const response = await api.put<PlayerStats>('/hub/stats', data);
  return response.data;
};

// ==================== Torre do Conhecimento (Skills) ====================

/**
 * Busca as habilidades disponíveis
 */
export const getSkills = async (): Promise<SkillsResponse> => {
  const response = await api.get<SkillsResponse>('/hub/skills');
  return response.data;
};

/**
 * Compra uma habilidade
 */
export const purchaseSkill = async (
  data: PurchaseSkillRequest
): Promise<PurchaseSkillResponse> => {
  const response = await api.post<PurchaseSkillResponse>('/hub/skills/purchase', data);
  return response.data;
};

// ==================== Biblioteca Silenciosa (Books) ====================

/**
 * Busca a lista de livros
 */
export const getBooks = async (): Promise<BooksResponse> => {
  const response = await api.get<BooksResponse>('/hub/books');
  return response.data;
};

/**
 * Busca os detalhes de um livro específico
 */
export const getBookDetails = async (bookId: string): Promise<BookDetails> => {
  const response = await api.get<BookDetails>(`/hub/books/${bookId}`);
  return response.data;
};

// ==================== Palco da Retórica (Professors) ====================

/**
 * Busca a lista de professores
 */
export const getProfessors = async (): Promise<ProfessorsResponse> => {
  const response = await api.get<ProfessorsResponse>('/hub/professors');
  return response.data;
};

/**
 * Busca os diálogos de um professor específico
 */
export const getProfessorDialogues = async (
  professorId: string
): Promise<ProfessorDialoguesResponse> => {
  const response = await api.get<ProfessorDialoguesResponse>(
    `/hub/professors/${professorId}/dialogues`
  );
  return response.data;
};

// ==================== Sebo da Linguística (Store) ====================

/**
 * Busca os itens disponíveis na loja
 */
export const getStoreItems = async (): Promise<StoresResponse> => {
  const response = await api.get<StoresResponse>('/hub/stores');
  return response.data;
};

/**
 * Compra um item da loja
 */
export const purchaseItem = async (
  data: PurchaseItemRequest
): Promise<PurchaseItemResponse> => {
  const response = await api.post<PurchaseItemResponse>('/hub/stores/purchase', data);
  return response.data;
};

export default {
  // Stats
  getHubStats,
  getAchievements,
  getBattleHistoryFromHub,
  getRankingsFromHub,
  updateHubStats,
  // Skills
  getSkills,
  purchaseSkill,
  // Books
  getBooks,
  getBookDetails,
  // Professors
  getProfessors,
  getProfessorDialogues,
  // Store
  getStoreItems,
  purchaseItem,
};
