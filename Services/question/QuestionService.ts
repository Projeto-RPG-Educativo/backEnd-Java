import api from '../api';
import type { 
    RandomQuestionResponse, 
    QuestionFromBackend } from '../../types/Question';

/**
 * Busca uma pergunta aleatória
 */
export const getRandomQuestion = async (): Promise<QuestionFromBackend> => {
  const response = await api.get<RandomQuestionResponse>('/perguntas/aleatoria');
  return response.data.question;
};

export default {
  getRandomQuestion,
};
