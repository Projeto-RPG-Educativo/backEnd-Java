package com.game.rpgbackend.dto.request.hub;

/**
 * DTO de requisição para compra de habilidade na Torre do Conhecimento.
 * <p>
 * Contém o ID da habilidade que o jogador deseja desbloquear.
 * A compra requer pontos de habilidade ou ouro suficientes.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
public class PurchaseSkillRequest {

    /** ID da habilidade a ser comprada */
    private Integer skillId;

    /**
     * Construtor padrão.
     */
    public PurchaseSkillRequest() {}

    /**
     * Construtor com ID da habilidade.
     *
     * @param skillId ID da habilidade
     */
    public PurchaseSkillRequest(Integer skillId) {
        this.skillId = skillId;
    }

    /** @return ID da habilidade */
    public Integer getSkillId() {
        return skillId;
    }

    /** @param skillId ID da habilidade */
    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }
}
