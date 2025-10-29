package com.game.rpgbackend.dto.request.hub;

public class PurchaseItemRequest {
    private Integer lojaId;
    private Integer itemId;

    public PurchaseItemRequest() {}

    public PurchaseItemRequest(Integer lojaId, Integer itemId) {
        this.lojaId = lojaId;
        this.itemId = itemId;
    }

    public Integer getLojaId() {
        return lojaId;
    }

    public void setLojaId(Integer lojaId) {
        this.lojaId = lojaId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
