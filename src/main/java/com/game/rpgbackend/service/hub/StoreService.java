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
 * Serviço responsável pelo Sebo da Linguística (Loja).
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final ItemLojaRepository itemLojaRepository;
    private final PlayerStatsRepository playerStatsRepository;

    /**
     * Busca todas as lojas disponíveis.
     */
    public List<Store> getStores() {
        return storeRepository.findAll();
    }

    /**
     * Busca os itens de uma loja específica.
     */
    public List<ItemStore> getStoreItems(Integer lojaId) {
        return itemLojaRepository.findByStoreId(lojaId);
    }

    /**
     * Processa a compra de um item da loja.
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

    // Classe interna para resultado de compra
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
