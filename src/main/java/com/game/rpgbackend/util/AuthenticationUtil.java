package com.game.rpgbackend.util;

import com.game.rpgbackend.domain.User;
import com.game.rpgbackend.exception.UnauthorizedException;
import com.game.rpgbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utilitário para obter informações do usuário autenticado.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationUtil {

    private final UserRepository userRepository;

    /**
     * Obtém o usuário autenticado atual.
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByNomeUsuario(username)
                    .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));
        }

        throw new UnauthorizedException("Usuário não autenticado");
    }

    /**
     * Obtém o ID do usuário autenticado atual.
     */
    public Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Obtém o ID do usuário a partir do username.
     */
    public Integer getUserIdFromUsername(String username) {
        return userRepository.findByNomeUsuario(username)
                .map(User::getId)
                .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));
    }
}

