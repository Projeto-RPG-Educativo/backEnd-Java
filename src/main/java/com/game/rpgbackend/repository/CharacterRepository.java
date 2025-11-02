package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository para operações de persistência de personagens.
 * <p>
 * Fornece métodos para buscar, salvar, atualizar e deletar personagens,
 * além de consultas customizadas para filtrar por usuário.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {

    /**
     * Busca todos os personagens de um usuário específico.
     *
     * @param userId identificador do usuário
     * @return lista de personagens do usuário
     */
    List<Character> findByUserId(Integer userId);

    /**
     * Busca todos os personagens de um usuário ordenados por ID decrescente.
     * <p>
     * Os personagens mais recentes aparecem primeiro.
     * </p>
     *
     * @param userId identificador do usuário
     * @return lista ordenada de personagens do usuário
     */
    List<Character> findByUserIdOrderByIdDesc(Integer userId);
}
