import api from '../api';
import type { 
  User, 
  RegisterUserRequest, 
  LoginUserRequest, 
  AuthResponse 
} from '../../types/User';

/**
 * Registra um novo usuário
 */
export const registerUser = async (data: RegisterUserRequest): Promise<AuthResponse> => {
  const response = await api.post<AuthResponse>('/users/register', data);
  
  // Armazena o token no localStorage
  if (response.data.token) {
    localStorage.setItem('token', response.data.token);
  }
  
  return response.data;
};

/**
 * Faz login de um usuário
 */
export const loginUser = async (data: LoginUserRequest): Promise<AuthResponse> => {
  const response = await api.post<AuthResponse>('/users/login', data);
  
  // Armazena o token no localStorage
  if (response.data.token) {
    localStorage.setItem('token', response.data.token);
  }
  
  return response.data;
};

/**
 * Busca o usuário atual (autenticado)
 */
export const getCurrentUser = async (): Promise<User> => {
  const response = await api.get<User>('/users/me');
  return response.data;
};

/**
 * Busca um usuário pelo ID
 */
export const getUserById = async (userId: number | string): Promise<User> => {
  const response = await api.get<User>(`/users/${userId}`);
  return response.data;
};

/**
 * Atualiza os dados do usuário
 */
export const updateUser = async (userId: number | string, data: Partial<User>): Promise<User> => {
  const response = await api.put<User>(`/users/${userId}`, data);
  return response.data;
};

/**
 * Deleta um usuário
 */
export const deleteUser = async (userId: number | string): Promise<{ message: string }> => {
  await api.delete(`/users/${userId}`);
  return { message: 'Usuário deletado com sucesso' };
};

export default {
  registerUser,
  loginUser,
  getCurrentUser,
  getUserById,
  updateUser,
  deleteUser,
};
