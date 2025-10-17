package com.game.rpgbackend.util;

import com.game.rpgbackend.domain.Character;
import com.game.rpgbackend.dto.response.CharacterDTO;
import com.game.rpgbackend.dto.response.CharacterListDTO;
import org.springframework.stereotype.Component;

/**
 * Classe utilitária para converter entidades Character em DTOs.
 */
@Component
public class CharacterMapper {

    /**
     * Converte uma entidade Character em CharacterDTO completo.
     */
    public CharacterDTO toDTO(Character character) {
        if (character == null) {
            return null;
        }

        CharacterDTO dto = new CharacterDTO();
        dto.setId(character.getId());
        dto.setNome(character.getNome());
        dto.setXp(character.getXp());
        dto.setGold(character.getGold());
        dto.setHp(character.getHp());
        dto.setEnergy(character.getEnergy());
        dto.setMaxEnergy(character.getMaxEnergy());
        dto.setLastSavedAt(character.getLastSavedAt());

        // Informações do usuário sem incluir a entidade completa
        if (character.getUser() != null) {
            dto.setUserId(character.getUser().getId());
            dto.setUserName(character.getUser().getNomeUsuario());
        }

        // Informações da classe sem incluir a entidade completa
        if (character.getGameClass() != null) {
            dto.setClassId(character.getGameClass().getId());
            dto.setClassName(character.getGameClass().getName());
        }

        return dto;
    }

    /**
     * Converte uma entidade Character em CharacterListDTO simplificado.
     */
    public CharacterListDTO toListDTO(Character character) {
        if (character == null) {
            return null;
        }

        CharacterListDTO dto = new CharacterListDTO();
        dto.setId(character.getId());
        dto.setNome(character.getNome());
        dto.setXp(character.getXp());
        dto.setGold(character.getGold());
        dto.setHp(character.getHp());

        if (character.getGameClass() != null) {
            dto.setClassName(character.getGameClass().getName());
        }

        return dto;
    }
}

