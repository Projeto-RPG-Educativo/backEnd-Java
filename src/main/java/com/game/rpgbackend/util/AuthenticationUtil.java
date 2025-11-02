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
 * <p>
 * Fornece métodos convenientes para acessar dados do usuário autenticado
 * a partir do contexto de segurança do Spring Security.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class AuthenticationUtil {

    private final UserRepository userRepository;

    /**
     * Obtém a entidade User completa do usuário autenticado atual.
     *
     * @return entidade User do usuário autenticado
     * @throws UnauthorizedException se não houver usuário autenticado
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));
        }

        throw new UnauthorizedException("Usuário não autenticado");
    }

    /**
     * Obtém apenas o ID do usuário autenticado atual.
     *
     * @return ID do usuário autenticado
     * @throws UnauthorizedException se não houver usuário autenticado
     */
    public Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /**
     * Obtém o ID de um usuário a partir do seu username.
     * <p>
     * Útil para operações que recebem username via @AuthenticationPrincipal.
     * </p>
     *
     * @param username nome de usuário
     * @return ID do usuário
     * @throws UnauthorizedException se o usuário não for encontrado
     */
    public Integer getUserIdFromUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));
    }
}
