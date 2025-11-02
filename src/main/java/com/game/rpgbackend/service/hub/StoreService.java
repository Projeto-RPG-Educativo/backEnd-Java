package com.game.rpgbackend.service.hub;

import com.game.rpgbackend.domain.ItemStore;
import com.game.rpgbackend.domain.Store;
import com.game.rpgbackend.domain.PlayerStats;
import com.game.rpgbackend.exception.BadRequestException;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.ItemLojaRepository;
import com.game.rpgbackend.repository.StoreRepository;
import com.game.rpgbackend.repository.PlayerStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela gestão do Sebo da Linguística (Sistema de Lojas).
 * <p>
 * O Sebo da Linguística é o estabelecimento comercial do Hub onde jogadores
 * podem comprar itens usando ouro acumulado durante o jogo. Gerencia todas
 * as operações comerciais incluindo listagem de produtos, verificação de
 * estoque, validação de moeda e processamento de transações.
 * </p>
 * <p>
 * Funcionalidades principais:
 * - Listar lojas disponíveis no jogo
 * - Exibir inventário de itens de cada loja
 * - Processar compras com verificação de saldo e estoque
 * - Atualizar inventário do jogador após compra
 * - Gerenciar estoque das lojas
 * </p>
 * <p>
 * Sistema de transações:
 * - Verifica ouro suficiente antes da compra
 * - Valida disponibilidade de estoque
 * - Deduz ouro do jogador automaticamente
 * - Adiciona item ao inventário do personagem
 * - Atualiza estoque da loja
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final ItemLojaRepository itemLojaRepository;
    private final PlayerStatsRepository playerStatsRepository;

    /**
     * Retorna todas as lojas disponíveis no Sebo da Linguística.
     * <p>
     * Lista completa de estabelecimentos comerciais onde o jogador
     * pode realizar compras. Cada loja pode ter um inventário
     * especializado (armas, armaduras, consumíveis, livros, etc.).
     * </p>
     *
     * @return lista de todas as lojas ativas no jogo
     */
    public List<Store> getStores() {
        return storeRepository.findAll();
    }

    /**
     * Busca o inventário completo de uma loja específica.
     * <p>
     * Retorna todos os itens disponíveis para compra na loja especificada,
     * incluindo:
     * - Informações detalhadas do item
     * - Preço em ouro
     * - Quantidade em estoque
     * - Descrição e efeitos
     * </p>
     * <p>
     * Apenas itens com estoque disponível devem ser exibidos ao jogador.
     * </p>
     *
     * @param lojaId identificador único da loja
     * @return lista de itens disponíveis na loja com preços e estoque
     */
    public List<ItemStore> getStoreItems(Integer lojaId) {
        return itemLojaRepository.findByStoreId(lojaId);
    }

    /**
     * Processa uma transação de compra de item na loja.
     * <p>
     * Executa todas as validações necessárias e efetiva a compra:
     * 1. Valida existência do jogador e do item
     * 2. Verifica se o jogador possui ouro suficiente
     * 3. Verifica disponibilidade em estoque
     * 4. Deduz o preço do ouro do jogador
     * 5. Adiciona o item ao inventário do jogador
     * 6. Atualiza o estoque da loja (reduz em 1)
     * 7. Persiste todas as alterações no banco
     * </p>
     * <p>
     * Operação transacional: Em caso de erro, todas as alterações
     * são revertidas automaticamente (rollback).
     * </p>
     *
     * @param userId identificador único do usuário comprador
     * @param lojaId identificador da loja onde a compra está sendo realizada
     * @param itemId identificador do item a ser comprado
     * @return resultado da compra com item adquirido, preço pago e ouro restante
     * @throws NotFoundException se jogador, loja ou item não forem encontrados
     * @throws BadRequestException se ouro for insuficiente ou item sem estoque
     */
    @Transactional
    public PurchaseResult purchaseStoreItem(Integer userId, Integer lojaId, Integer itemId) {
        // Busca as estatísticas do jogador
        PlayerStats player = playerStatsRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Jogador não encontrado"));

        // Busca o item na loja
        ItemStore itemStore = itemLojaRepository.findByStoreId(lojaId).stream()
            .filter(il -> il.getItemId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Item não encontrado na loja"));

        // Verifica se o jogador tem ouro suficiente
        if (player.getTotalGoldEarned() < itemStore.getPrice()) {
            throw new BadRequestException("Ouro insuficiente para comprar este item");
        }

        // Verifica se há estoque
        if (itemStore.getQuantity() <= 0) {
            throw new BadRequestException("Item sem estoque");
        }

        // Deduz o ouro do jogador
        player.setTotalGoldEarned(player.getTotalGoldEarned() - itemStore.getPrice());
        playerStatsRepository.save(player);

        // Reduz o estoque (opcional, dependendo da lógica do jogo)
        itemStore.setQuantity(itemStore.getQuantity() - 1);
        itemLojaRepository.save(itemStore);

        return new PurchaseResult(itemStore.getItem(), itemStore.getPrice(), player.getTotalGoldEarned());
    }

    /**
     * DTO de resposta para resultado de compra na loja.
     * <p>
     * Contém todas as informações sobre a transação realizada:
     * - Item comprado
     * - Preço pago
     * - Ouro restante do jogador
     * </p>
     */
    public static class PurchaseResult {
        private com.game.rpgbackend.domain.Item item;
        private Integer pricePaid;
        private Integer remainingGold;

        public PurchaseResult(com.game.rpgbackend.domain.Item item, Integer pricePaid, Integer remainingGold) {
            this.item = item;
            this.pricePaid = pricePaid;
            this.remainingGold = remainingGold;
        }

        public com.game.rpgbackend.domain.Item getItem() { return item; }
        public Integer getPricePaid() { return pricePaid; }
        public Integer getRemainingGold() { return remainingGold; }
    }
}
