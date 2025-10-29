package com.game.rpgbackend.dto.request.hub;

public class PurchaseSkillRequest {
    private Integer skillId;

    public PurchaseSkillRequest() {}

    public PurchaseSkillRequest(Integer skillId) {
        this.skillId = skillId;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }
}
