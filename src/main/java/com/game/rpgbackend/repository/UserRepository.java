package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNomeUsuario(String nomeUsuario);
    Optional<User> findByEmail(String email);
    boolean existsByNomeUsuario(String nomeUsuario);
    boolean existsByEmail(String email);
}
