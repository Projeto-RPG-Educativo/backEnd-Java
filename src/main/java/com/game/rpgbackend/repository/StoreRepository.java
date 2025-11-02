package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para operações de persistência de lojas do Sebo da Linguística.
 * <p>
 * Gerencia o acesso aos estabelecimentos comerciais onde jogadores
 * podem comprar itens usando ouro. Fornece operações CRUD padrão do JPA.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
}
