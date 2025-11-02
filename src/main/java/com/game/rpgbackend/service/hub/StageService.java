package com.game.rpgbackend.service.hub;

import com.game.rpgbackend.domain.Dialogue;
import com.game.rpgbackend.domain.NPC;
import com.game.rpgbackend.repository.DialogueRepository;
import com.game.rpgbackend.repository.NPCRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela gestão do Palco da Retórica.
 * <p>
 * O Palco da Retórica é um local no Hub onde jogadores podem interagir
 * com NPCs (Non-Player Characters) e participar de diálogos educacionais.
 * Gerencia todos os aspectos relacionados a NPCs e suas conversas.
 * </p>
 * <p>
 * Funcionalidades principais:
 * - Listar NPCs disponíveis para interação
 * - Buscar diálogos específicos de cada NPC
 * - Filtrar NPCs por tipo (mercador, mentor, guarda, etc.)
 * - Filtrar NPCs por localização no jogo
 * </p>
 * <p>
 * Os NPCs são personagens controlados pelo sistema que fornecem:
 * - Diálogos educacionais
 * - Quests e missões
 * - Informações sobre o lore do jogo
 * - Serviços (vendas, treinamento, etc.)
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StageService {

    private final NPCRepository npcRepository;
    private final DialogueRepository dialogueRepository;

    /**
     * Retorna todos os NPCs disponíveis no Palco da Retórica.
     * <p>
     * Lista completa de todos os personagens não-jogáveis com os quais
     * o jogador pode interagir, incluindo suas informações básicas:
     * - Nome e descrição
     * - Tipo (mercador, mentor, guarda, etc.)
     * - Localização no jogo
     * </p>
     *
     * @return lista completa de NPCs disponíveis
     */
    public List<NPC> getNPCs() {
        return npcRepository.findAll();
    }

    /**
     * Busca todos os diálogos de um NPC específico.
     * <p>
     * Retorna a lista completa de conversas que o jogador pode ter
     * com o NPC especificado. Cada diálogo pode conter:
     * - Conteúdo da fala do NPC
     * - Possíveis respostas do jogador
     * - Condições para exibição (nível, quests completadas, etc.)
     * </p>
     * <p>
     * Os diálogos são ordenados conforme definido no banco de dados,
     * geralmente seguindo uma sequência lógica de conversação.
     * </p>
     *
     * @param npcId identificador único do NPC
     * @return lista de diálogos do NPC especificado
     */
    public List<Dialogue> getNPCDialogues(Integer npcId) {
        return dialogueRepository.findByNpcId(npcId);
    }

    /**
     * Busca NPCs filtrados por tipo específico.
     * <p>
     * Permite encontrar NPCs com funções específicas no jogo:
     * - "mercador": NPCs que vendem itens
     * - "mentor": NPCs que dão conselhos e ensinam
     * - "guarda": NPCs que protegem áreas específicas
     * - "questgiver": NPCs que oferecem missões
     * - Outros tipos personalizados
     * </p>
     * <p>
     * Útil para encontrar rapidamente NPCs que oferecem serviços específicos.
     * </p>
     *
     * @param type categoria/função do NPC
     * @return lista de NPCs do tipo especificado
     */
    public List<NPC> getNPCsByType(String type) {
        return npcRepository.findByType(type);
    }

    /**
     * Busca NPCs presentes em uma localização específica do jogo.
     * <p>
     * Retorna todos os NPCs que podem ser encontrados em um determinado
     * local, como:
     * - "Hub Central"
     * - "Biblioteca Silenciosa"
     * - "Torre do Conhecimento"
     * - "Arena de Batalhas"
     * - "Mercado"
     * </p>
     * <p>
     * Permite ao frontend exibir apenas os NPCs relevantes para a
     * localização atual do jogador.
     * </p>
     *
     * @param location nome da localização no jogo
     * @return lista de NPCs presentes na localização especificada
     */
    public List<NPC> getNPCsByLocation(String location) {
        return npcRepository.findByLocation(location);
    }
}

