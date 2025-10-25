import api from '../api';
import type { Player, ClassName } from '../../types/Entity';

// ==================== INTERFACES ====================
export interface CreateCharacterRequest {
  nome: string;
  classe: ClassName;
}

export interface UpdateCharacterRequest {
  nome?: string;
  classe?: ClassName;
  hp?: number;
  maxHp?: number;
  stamina?: number;
  maxStamina?: number;
  level?: number;
  coins?: number;
}

export interface CharacterResponse {
  id: number;
  userId: number;
  nome: string;
  classe: string;
  image?: string;
  hp: number;
  maxHp: number;
  stamina: number;
  maxStamina: number;
  level: number;
  coins: number;
  createdAt: string;
}

// Interface para as classes disponíveis
export interface ClassResponse {
  id: number;
  name: string;
  hp: number;
  stamina: number | null;
  strength: number | null;
  intelligence: number | null;
}

// Alias para Character (compatibilidade com hooks)
export type Character = CharacterResponse;

// ==================== BUSCAR CLASSES DISPONÍVEIS ====================
export const getAvailableClasses = async (): Promise<ClassResponse[]> => {
  try {
    console.log('🎭 [CharacterService] Buscando classes disponíveis...');
    
    const response = await api.get<ClassResponse[]>('/classes');
    
    console.log(`✅ [CharacterService] ${response.data.length} classes encontradas.`);
    return response.data;
  } catch (error: any) {
    console.error('❌ [CharacterService] Erro ao buscar classes:', error.response?.data);
    throw new Error(error.response?.data?.message || 'Erro ao buscar classes disponíveis');
  }
};

// ==================== LISTAR PERSONAGENS ====================
export const listCharacters = async (): Promise<CharacterResponse[]> => {
  try {
    console.log('📂 [CharacterService] Listando personagens...');
    
    const response = await api.get<CharacterResponse[]>('/api/classes');
    
    console.log(`✅ [CharacterService] ${response.data.length} personagens encontrados.`);
    return response.data;
  } catch (error: any) {
    console.error('❌ [CharacterService] Erro ao listar personagens:', error.response?.data);
    throw new Error(error.response?.data?.message || 'Erro ao listar personagens');
  }
};

// ==================== CRIAR PERSONAGEM ====================
export const createCharacter = async (data: CreateCharacterRequest): Promise<CharacterResponse> => {
  try {
    console.log('➕ [CharacterService] Criando personagem...', data);
    
    const response = await api.post<CharacterResponse>('/character', data);
    
    console.log('✅ [CharacterService] Personagem criado:', response.data.nome);
    return response.data;
  } catch (error: any) {
    console.error('❌ [CharacterService] Erro ao criar personagem:', error.response?.data);
    throw new Error(error.response?.data?.message || 'Erro ao criar personagem');
  }
};

// ==================== BUSCAR PERSONAGEM POR ID ====================
export const getCharacterById = async (id: number): Promise<CharacterResponse> => {
  try {
    console.log(`🔍 [CharacterService] Buscando personagem ID ${id}...`);
    
    const response = await api.get<CharacterResponse>(`/character/${id}`);
    
    console.log('✅ [CharacterService] Personagem encontrado:', response.data.nome);
    return response.data;
  } catch (error: any) {
    console.error('❌ [CharacterService] Erro ao buscar personagem:', error.response?.data);
    throw new Error(error.response?.data?.message || 'Erro ao buscar personagem');
  }
};

// Alias para getCharacter (compatibilidade com hooks)
export const getCharacter = getCharacterById;

// ==================== ATUALIZAR PERSONAGEM ====================
export const updateCharacter = async (
  id: number | string,
  data: UpdateCharacterRequest
): Promise<CharacterResponse> => {
  try {
    console.log(`✏️ [CharacterService] Atualizando personagem ID ${id}...`, data);
    
    const response = await api.put<CharacterResponse>(`/character/save-progress/${id}`, data);
    
    console.log('✅ [CharacterService] Personagem atualizado:', response.data.nome);
    return response.data;
  } catch (error: any) {
    console.error('❌ [CharacterService] Erro ao atualizar personagem:', error.response?.data);
    throw new Error(error.response?.data?.message || 'Erro ao atualizar personagem');
  }
};

// ==================== DELETAR PERSONAGEM ====================
export const deleteCharacter = async (id: number | string): Promise<{ message: string }> => {
  try {
    console.log(`🗑️ [CharacterService] Deletando personagem ID ${id}...`);
    
    await api.delete(`/characters/${id}`);
    
    console.log('✅ [CharacterService] Personagem deletado.');
    return { message: 'Personagem deletado com sucesso' };
  } catch (error: any) {
    console.error('❌ [CharacterService] Erro ao deletar personagem:', error.response?.data);
    throw new Error(error.response?.data?.message || 'Erro ao deletar personagem');
  }
};

// ==================== CONVERTER PARA PLAYER (Helper) ====================
export const convertToPlayer = (char: CharacterResponse): Player => {
  return {
    id: char.id,
    name: char.nome,
    className: char.classe as ClassName,
    hp: char.hp,
    maxHp: char.maxHp,
    stamina: char.stamina,
    maxStamina: char.maxStamina,
    damage: 10, // Valor padrão, ajustar conforme necessário
    level: char.level,
    coins: char.coins,
    abilityUsed: false,
    image: char.image || '',
  };
};
