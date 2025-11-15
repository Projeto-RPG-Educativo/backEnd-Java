package com.game.rpgbackend.config;

import com.game.rpgbackend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuração de segurança da aplicação Spring Security.
 * <p>
 * Define as políticas de segurança do sistema, incluindo:
 * - Autenticação via JWT (JSON Web Tokens)
 * - Autorização de endpoints (públicos vs protegidos)
 * - Configuração CORS para frontend
 * - Criptografia de senhas com BCrypt
 * - Gerenciamento de sessões stateless
 * </p>
 * <p>
 * Endpoints públicos (sem autenticação):
 * - /api/auth/** (registro e login)
 * - /api/classes/** (consulta de classes de personagem)
 * </p>
 * <p>
 * Todos os outros endpoints requerem token JWT válido no header Authorization.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configura a cadeia de filtros de segurança do Spring Security.
     * <p>
     * Configurações aplicadas:
     * - CSRF desabilitado (API REST stateless)
     * - CORS habilitado para frontend React
     * - Sessões stateless (sem cookies de sessão)
     * - Endpoints públicos definidos
     * - Filtro JWT adicionado antes da autenticação padrão
     * </p>
     *
     * @param http objeto HttpSecurity para configuração
     * @return cadeia de filtros configurada
     * @throws Exception se houver erro na configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/classes/**").permitAll()
                        // Todos os outros endpoints requerem autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura o CORS (Cross-Origin Resource Sharing) da aplicação.
     * <p>
     * Permite requisições dos frontends em desenvolvimento:
     * - http://localhost:3000 (Create React App padrão)
     * - http://localhost:5173 (Vite padrão)
     * </p>
     * <p>
     * Métodos HTTP permitidos: GET, POST, PUT, DELETE, OPTIONS
     * </p>
     *
     * @return fonte de configuração CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173", "https://frontend-client-jaal.onrender.com")); // Frontend URLs
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Define o codificador de senhas usado para criptografia.
     * <p>
     * Utiliza BCrypt, um algoritmo de hash adaptativo que:
     * - Adiciona salt automático
     * - Aumenta o custo computacional ao longo do tempo
     * - É resistente a ataques de força bruta
     * </p>
     *
     * @return codificador BCrypt configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura o gerenciador de autenticação do Spring Security.
     * <p>
     * Usado internamente para validar credenciais de usuário
     * durante o processo de login.
     * </p>
     *
     * @param config configuração de autenticação do Spring
     * @return gerenciador de autenticação
     * @throws Exception se houver erro na configuração
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

