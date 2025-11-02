package com.game.rpgbackend.dto.request.hub;

/**
 * DTO de requisição para compra de item em uma loja.
 * <p>
 * Contém as informações necessárias para realizar uma compra:
 * a loja onde o item está sendo vendido e o item específico a ser comprado.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
public class PurchaseItemRequest {

    /** ID da loja onde o item será comprado */
    private Integer lojaId;

    /** ID do item a ser comprado */
    private Integer itemId;

    /**
     * Construtor padrão.
     */
    public PurchaseItemRequest() {}

    /**
     * Construtor com todos os campos.
     *
     * @param lojaId ID da loja
     * @param itemId ID do item
     */
    public PurchaseItemRequest(Integer lojaId, Integer itemId) {
        this.lojaId = lojaId;
        this.itemId = itemId;
    }

    /** @return ID da loja */
    public Integer getLojaId() {
        return lojaId;
    }

    /** @param lojaId ID da loja */
    public void setLojaId(Integer lojaId) {
        this.lojaId = lojaId;
    }

    /** @return ID do item */
    public Integer getItemId() {
        return itemId;
    }

    /** @param itemId ID do item */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
