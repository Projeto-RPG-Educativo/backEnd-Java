package com.game.rpgbackend.util;

import com.game.rpgbackend.domain.Character;
import com.game.rpgbackend.dto.response.CharacterDTO;
import com.game.rpgbackend.dto.response.CharacterListDTO;
import org.springframework.stereotype.Component;

/**
 * Classe utilitária para converter entidades Character em DTOs.
 * <p>
 * Realiza o mapeamento entre as entidades de domínio e os DTOs de resposta,
 * evitando referências circulares e expondo apenas as informações necessárias.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Component
public class CharacterMapper {

    /**
     * Converte uma entidade Character em CharacterDTO completo.
     * <p>
     * Inclui todos os atributos do personagem e informações básicas
     * do usuário e classe associados, evitando referências circulares.
     * </p>
     *
     * @param character entidade Character a ser convertida
     * @return DTO completo do personagem ou null se character for null
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
     * <p>
     * Versão reduzida contendo apenas informações essenciais para listagens,
     * melhorando a performance ao reduzir a quantidade de dados trafegados.
     * </p>
     *
     * @param character entidade Character a ser convertida
     * @return DTO simplificado do personagem ou null se character for null
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
