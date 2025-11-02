package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório para operações de persistência da entidade User.
 * <p>
 * Fornece métodos de consulta customizados para buscar usuários
 * por username e email, além das operações CRUD padrão do JPA.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Busca um usuário pelo username.
     *
     * @param username nome de usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca um usuário pelo email.
     *
     * @param email endereço de email
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica se existe um usuário com o username fornecido.
     *
     * @param username nome de usuário
     * @return true se existir, false caso contrário
     */
    boolean existsByUsername(String username);

    /**
     * Verifica se existe um usuário com o email fornecido.
     *
     * @param email endereço de email
     * @return true se existir, false caso contrário
     */
    boolean existsByEmail(String email);
}
