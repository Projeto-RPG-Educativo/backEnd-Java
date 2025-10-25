import api from '../api';
import type { AuthResponse } from '../../types/User';

// ==================== LOGIN ====================
/**
 * Realiza login do usuário
 * @param email Email do usuário
 * @param senha Senha do usuário
 * @returns Token e dados do usuário
 */
export const login = async (usuario: string, senha: string): Promise<AuthResponse> => {
  try {
    console.log('🔐 [AuthService] Fazendo login...');

    const response = await api.post<AuthResponse>('/auth/login', {
      nomeUsuario: usuario,
      senha: senha,
    });
    
    // Salvar token no localStorage
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      console.log('✅ [AuthService] Login bem-sucedido. Token salvo.');
    }
    
    return response.data;
  } catch (error: any) {
    console.error('❌ [AuthService] Erro ao fazer login:', error.response?.data);
    throw new Error(error.response?.data?.message || 'Erro ao fazer login');
  }
};

// ==================== REGISTRO ====================
/**
 * Registra um novo usuário
 * @param nome Nome do usuário
 * @param email Email do usuário
 * @param senha Senha do usuário
 * @returns Token e dados do usuário
 */
export const register = async (
  nome: string,
  email: string,
  senha: string
): Promise<AuthResponse> => {
  try {
    console.log('📝 [AuthService] Registrando novo usuário...');
    
    const response = await api.post<AuthResponse>('/auth/register', {
      nomeUsuario: nome,
      email,
      senha: senha,
    });
    
    // Salvar token no localStorage
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      console.log('✅ [AuthService] Registro bem-sucedido. Token salvo.');
    }
    
    return response.data;
  } catch (error: any) {
    console.error('❌ [AuthService] Erro ao registrar:', error.response?.data);
    throw new Error(error.response?.data?.message || 'Erro ao registrar usuário');
  }
};

// ==================== LOGOUT ====================
/**
 * Faz logout do usuário (remove token do localStorage)
 */
export const logout = (): void => {
  console.log('👋 [AuthService] Fazendo logout...');
  localStorage.removeItem('token');
  console.log('✅ [AuthService] Token removido. Logout concluído.');
};

// ==================== VERIFICAR AUTENTICAÇÃO ====================
/**
 * Verifica se o usuário está autenticado (possui token)
 * @returns true se autenticado, false caso contrário
 */
export const isAuthenticated = (): boolean => {
  const token = localStorage.getItem('token');
  return token !== null && token !== '';
};

// ==================== OBTER TOKEN ====================
/**
 * Retorna o token de autenticação armazenado
 * @returns Token ou null
 */
export const getToken = (): string | null => {
  return localStorage.getItem('token');
};

export default {
  login,
  register,
  logout,
  isAuthenticated,
  getToken,
};
