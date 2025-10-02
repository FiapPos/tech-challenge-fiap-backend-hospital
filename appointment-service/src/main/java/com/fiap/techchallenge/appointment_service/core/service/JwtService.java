package com.fiap.techchallenge.appointment_service.core.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret:mySecretKey}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400}")
    private int jwtExpirationInSeconds;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Authentication authentication) {
        var username = authentication.getName();
        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String perfilTipo = null;
        if (authorities != null && !authorities.isBlank()) {
            String first = authorities.split(",")[0].trim();
            if (first.startsWith("ROLE_")) {
                perfilTipo = first.substring("ROLE_".length());
            } else if (first.startsWith("PERFIL_")) {
                perfilTipo = first.substring("PERFIL_".length());
            } else {
                perfilTipo = first;
            }
        }

        var now = new Date();
        var expiryDate = new Date(now.getTime() + jwtExpirationInSeconds * 1000L);

        return Jwts.builder()
                .subject(username)
                .claim("roles", authorities)

                .claim("perfil", perfilTipo)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (ExpiredJwtException eje) {
            var claims = eje.getClaims();
            return claims == null ? null : claims.getSubject();
        }
    }

    public LocalDateTime getExpirationFromToken(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (ExpiredJwtException eje) {
            var claims = eje.getClaims();
            if (claims == null || claims.getExpiration() == null)
                return null;
            return claims.getExpiration()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException eje) {
            log.debug("Token JWT expirado: {}", eje.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token JWT inválido: {}", e.getMessage());
            return false;
        }
    }

    public String getUserProfileFromToken(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return extractPerfilFromClaims(claims);
        } catch (ExpiredJwtException eje) {
            var claims = eje.getClaims();
            return extractPerfilFromClaims(claims);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Erro ao extrair perfil do token: {}", e.getMessage());
            return null;
        }
    }

    public String getRolesFromToken(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("roles", String.class);
        } catch (ExpiredJwtException eje) {
            var claims = eje.getClaims();
            return claims == null ? null : claims.get("roles", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Erro ao extrair roles do token: {}", e.getMessage());
            return null;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return extractUserIdFromClaims(claims);
        } catch (ExpiredJwtException eje) {
            var claims = eje.getClaims();
            return extractUserIdFromClaims(claims);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Erro ao extrair userId do token: {}", e.getMessage());
            return null;
        }
    }

    private String extractPerfilFromClaims(Claims claims) {
        if (claims == null)
            return null;
        String perfil = claims.get("perfil", String.class);
        if (perfil != null && !perfil.isBlank()) {
            return perfil.trim();
        }

        String roles = claims.get("roles", String.class);
        if (roles != null && !roles.isEmpty()) {
            String role = roles.split(",")[0].trim();
            if (role.startsWith("ROLE_")) {
                return role.substring("ROLE_".length());
            } else if (role.startsWith("PERFIL_")) {
                return role.substring("PERFIL_".length());
            }
            return role;
        }
        return null;
    }

    private Long extractUserIdFromClaims(Claims claims) {
        if (claims == null)
            return null;
        Object userIdObj = claims.get("userId");
        if (userIdObj == null)
            return null;
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(userIdObj));
        } catch (NumberFormatException ex) {
            log.debug("Não foi possível converter userId do token: {}", ex.getMessage());
            return null;
        }
    }
}