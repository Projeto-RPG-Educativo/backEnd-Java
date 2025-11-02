package com.game.rpgbackend.dto.request.character;

/**
 * DTO de requisição para criação de novo personagem.
 * <p>
 * Contém a classe que o personagem deve ter ao ser criado.
 * Classes disponíveis: lutador, mago, bardo, ladino, paladino, tank.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
public class CreateCharacterRequest {

    /**
     * Nome da classe do personagem a ser criado.
     * Exemplos: "lutador", "mago", "bardo", "ladino", "paladino", "tank"
     */
    private String classe;

    /**
     * Construtor padrão.
     */
    public CreateCharacterRequest() {}

    /**
     * Construtor com classe do personagem.
     *
     * @param classe nome da classe do personagem
     */
    public CreateCharacterRequest(String classe) {
        this.classe = classe;
    }

    /**
     * Obtém a classe do personagem.
     *
     * @return nome da classe
     */
    public String getClasse() {
        return classe;
    }

    /**
     * Define a classe do personagem.
     *
     * @param classe nome da classe
     */
    public void setClasse(String classe) {
        this.classe = classe;
    }
}
