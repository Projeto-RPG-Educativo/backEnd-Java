package com.game.rpgbackend.security;

import com.game.rpgbackend.domain.User;
import com.game.rpgbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Serviço customizado para carregar detalhes do usuário para autenticação.
 * <p>
 * Implementa a interface UserDetailsService do Spring Security para
 * integração com o sistema de autenticação. Busca usuários no banco
 * de dados e os converte para o formato UserDetails do Spring.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Carrega um usuário pelo nome de usuário para autenticação.
     * <p>
     * Busca o usuário no banco de dados e converte para UserDetails do Spring Security.
     * Atualmente não implementa roles/authorities específicos.
     * </p>
     *
     * @param username nome de usuário a ser buscado
     * @return objeto UserDetails com informações do usuário
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getHashedPassword(),
                new ArrayList<>() // Authorities/Roles (vazio por enquanto)
        );
    }
}
