package com.game.rpgbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilitário para geração e validação de tokens JWT.
 * <p>
 * Responsável por criar tokens JWT para autenticação de usuários,
 * validar tokens recebidos e extrair informações dos tokens.
 * Utiliza HMAC-SHA256 para assinatura dos tokens.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:sua_chave_secreta_padrao_muito_longa_para_ser_segura}")
    private String secret;

    @Value("${jwt.expiration:36000000}") // 10 horas em milissegundos
    private Long expiration;

    /**
     * Obtém a chave de assinatura para os tokens JWT.
     *
     * @return chave secreta para assinatura HMAC
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Gera um token JWT para um usuário.
     *
     * @param username nome de usuário a ser incluído no token
     * @return token JWT assinado
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Cria o token JWT com as claims e subject especificados.
     *
     * @param claims informações adicionais a incluir no token
     * @param subject identificador principal (username)
     * @return token JWT completo e assinado
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida se um token JWT é válido para um usuário específico.
     *
     * @param token token JWT a ser validado
     * @param username nome de usuário esperado
     * @return true se o token é válido e não expirou, false caso contrário
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Extrai o username (subject) do token JWT.
     *
     * @param token token JWT
     * @return username contido no token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrai a data de expiração do token JWT.
     *
     * @param token token JWT
     * @return data de expiração do token
     */
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * Extrai todas as claims (informações) do token JWT.
     *
     * @param token token JWT
     * @return objeto Claims com todas as informações do token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Verifica se o token JWT está expirado.
     *
     * @param token token JWT a verificar
     * @return true se o token expirou, false caso contrário
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
