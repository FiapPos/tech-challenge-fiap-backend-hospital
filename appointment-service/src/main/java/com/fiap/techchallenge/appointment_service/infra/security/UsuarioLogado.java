package com.fiap.techchallenge.appointment_service.infra.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UsuarioLogado {

    /**
     * Retorna true se o usuário autenticado tiver a authority ROLE_PACIENTE
     */
    public boolean ehPaciente() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_PACIENTE".equals(a.getAuthority()));
    }

    /**
     * Extrai o userId do token JWT (claim "userId"). Retorna null se não
     * encontrado.
     */
    public Long getIdUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return null;

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Object uid = jwtAuth.getToken().getClaim("userId");
            if (uid instanceof Number) {
                return ((Number) uid).longValue();
            }
            if (uid instanceof String) {
                try {
                    return Long.parseLong((String) uid);
                } catch (NumberFormatException ex) {
                    return null;
                }
            }
        }

        return null;
    }
}
