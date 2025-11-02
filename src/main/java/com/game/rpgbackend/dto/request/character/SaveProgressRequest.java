package com.game.rpgbackend.dto.request.character;

/**
 * DTO de requisição para salvar o progresso de um personagem.
 * <p>
 * Permite atualizar os valores de experiência (XP) e pontos de vida (HP)
 * de um personagem após batalhas ou outras atividades no jogo.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
public class SaveProgressRequest {

    /** ID do personagem cujo progresso será salvo */
    private Integer characterId;

    /** Quantidade de pontos de experiência do personagem */
    private Integer xp;

    /** Pontos de vida atual do personagem */
    private Integer hp;

    /**
     * Construtor padrão.
     */
    public SaveProgressRequest() {}

    /**
     * Construtor com todos os campos.
     *
     * @param characterId ID do personagem
     * @param xp experiência do personagem
     * @param hp vida do personagem
     */
    public SaveProgressRequest(Integer characterId, Integer xp, Integer hp) {
        this.characterId = characterId;
        this.xp = xp;
        this.hp = hp;
    }

    /**
     * Obtém o ID do personagem.
     *
     * @return ID do personagem
     */
    public Integer getCharacterId() {
        return characterId;
    }

    /**
     * Define o ID do personagem.
     *
     * @param characterId ID do personagem
     */
    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }

    /**
     * Obtém a experiência do personagem.
     *
     * @return pontos de experiência
     */
    public Integer getXp() {
        return xp;
    }

    /**
     * Define a experiência do personagem.
     *
     * @param xp pontos de experiência
     */
    public void setXp(Integer xp) {
        this.xp = xp;
    }

    /**
     * Obtém os pontos de vida do personagem.
     *
     * @return pontos de vida
     */
    public Integer getHp() {
        return hp;
    }

    /**
     * Define os pontos de vida do personagem.
     *
     * @param hp pontos de vida
     */
    public void setHp(Integer hp) {
        this.hp = hp;
    }
}
