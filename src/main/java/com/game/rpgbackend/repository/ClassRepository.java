package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.GameClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório para operações de persistência de classes de personagem.
 * <p>
 * Gerencia o acesso às classes jogáveis do sistema (Lutador, Mago, Bardo,
 * Ladino, Paladino, Tank), incluindo seus atributos base e características.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ClassRepository extends JpaRepository<GameClass, Integer> {

    /**
     * Busca uma classe de personagem pelo nome.
     *
     * @param name nome da classe (lutador, mago, bardo, etc.)
     * @return Optional contendo a classe se encontrada
     */
    Optional<GameClass> findByName(String name);
}
