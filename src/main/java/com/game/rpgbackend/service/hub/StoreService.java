package com.game.rpgbackend.service.hub;

import com.game.rpgbackend.domain.ItemLoja;
import com.game.rpgbackend.domain.Loja;
import com.game.rpgbackend.domain.PlayerStats;
import com.game.rpgbackend.exception.BadRequestException;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.ItemLojaRepository;
import com.game.rpgbackend.repository.LojaRepository;
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

    private final LojaRepository lojaRepository;
    private final ItemLojaRepository itemLojaRepository;
    private final PlayerStatsRepository playerStatsRepository;

    /**
     * Busca todas as lojas disponíveis.
     */
    public List<Loja> getStores() {
        return lojaRepository.findAll();
    }

    /**
     * Busca os itens de uma loja específica.
     */
    public List<ItemLoja> getStoreItems(Integer lojaId) {
        return itemLojaRepository.findByLojaId(lojaId);
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
        ItemLoja itemLoja = itemLojaRepository.findByLojaId(lojaId).stream()
            .filter(il -> il.getItemId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Item não encontrado na loja"));

        // Verifica se o jogador tem ouro suficiente
        if (player.getTotalOuroGanho() < itemLoja.getPreco()) {
            throw new BadRequestException("Ouro insuficiente para comprar este item");
        }

        // Verifica se há estoque
        if (itemLoja.getQuantidade() <= 0) {
            throw new BadRequestException("Item sem estoque");
        }

        // Deduz o ouro do jogador
        player.setTotalOuroGanho(player.getTotalOuroGanho() - itemLoja.getPreco());
        playerStatsRepository.save(player);

        // Reduz o estoque (opcional, dependendo da lógica do jogo)
        itemLoja.setQuantidade(itemLoja.getQuantidade() - 1);
        itemLojaRepository.save(itemLoja);

        return new PurchaseResult(itemLoja.getItem(), itemLoja.getPreco(), player.getTotalOuroGanho());
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

