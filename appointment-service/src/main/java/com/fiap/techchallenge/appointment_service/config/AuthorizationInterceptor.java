package com.fiap.techchallenge.appointment_service.config;

import com.fiap.techchallenge.appointment_service.core.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String path = request.getRequestURI();

        if (path.startsWith("/api/usuario/consulta") || path.startsWith("/api/agendamento/edicao")
                || path.startsWith("/api/agendamento/criacao")) {

            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                log.warn("Token JWT não encontrado ou formato inválido");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            String token = authHeader.substring(7);

            if (!jwtService.validateToken(token)) {
                log.warn("Token JWT inválido ou expirado");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            String userProfile = jwtService.getUserProfileFromToken(token);

            if (userProfile == null) {
                log.warn("Perfil de usuário não encontrado no token");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return false;
            }

            String allowed = "";

            if (path.startsWith("/api/usuario/consulta")) {
                allowed = "ADMIN,MEDICO,ENFERMEIRO,PACIENTE";

            } else if (path.startsWith("/api/agendamento/edicao")) {
                allowed = "ADMIN,MEDICO,ENFERMEIRO";

            } else if (path.startsWith("/api/agendamento/criacao")) {
                allowed = "ADMIN,MEDICO,ENFERMEIRO,PACIENTE";
            }

            List<String> allowedProfiles = Arrays.stream(allowed.split(",")).map(String::trim).toList();
            if (!allowedProfiles.contains(userProfile)) {
                log.warn("Perfil '{}' não autorizado para este endpoint. Perfis permitidos: {}", userProfile,
                        allowedProfiles);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return false;
            }
            request.setAttribute("X-User-Profile", userProfile);
            String username = jwtService.getUsernameFromToken(token);
            request.setAttribute("X-Username", username);

            log.debug("Autorização bem-sucedida para usuário '{}' com perfil '{}'", username, userProfile);
        }

        return true;
    }
}
