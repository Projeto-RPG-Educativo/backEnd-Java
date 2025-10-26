package com.game.rpgbackend.util;

import com.game.rpgbackend.domain.GameClass;
import com.game.rpgbackend.dto.response.GameClassDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para converter entidades GameClass em DTOs.
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Component
public class GameClassMapper {

    /**
     * Converte uma entidade GameClass para DTO.
     *
     * @param gameClass Entidade GameClass
     * @return DTO GameClassDTO
     */
    public GameClassDTO toDTO(GameClass gameClass) {
        if (gameClass == null) {
            return null;
        }

        return new GameClassDTO(
                gameClass.getId(),
                gameClass.getName(),
                gameClass.getHp(),
                gameClass.getStamina(),
                gameClass.getStrength(),
                gameClass.getIntelligence()
        );
    }

    /**
     * Converte uma lista de GameClass para lista de DTOs.
     *
     * @param gameClasses Lista de entidades GameClass
     * @return Lista de DTOs
     */
    public List<GameClassDTO> toDTOList(List<GameClass> gameClasses) {
        if (gameClasses == null) {
            return null;
        }

        return gameClasses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

