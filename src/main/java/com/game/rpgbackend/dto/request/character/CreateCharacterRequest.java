package com.game.rpgbackend.dto.request.character;

public class CreateCharacterRequest {
    private String classe;

    public CreateCharacterRequest() {}

    public CreateCharacterRequest(String classe) {
        this.classe = classe;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
}
