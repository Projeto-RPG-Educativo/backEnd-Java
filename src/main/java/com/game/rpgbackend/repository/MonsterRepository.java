package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Monster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para operações de persistência de monstros.
 * <p>
 * Fornece acesso aos monstros disponíveis no sistema de batalhas,
 * incluindo seus atributos de HP, dano e defesa.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface MonsterRepository extends JpaRepository<Monster, Integer> {
}
