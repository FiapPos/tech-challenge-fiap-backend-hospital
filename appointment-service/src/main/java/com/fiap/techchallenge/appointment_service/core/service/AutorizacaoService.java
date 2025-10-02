package com.fiap.techchallenge.appointment_service.core.service;

import com.fiap.techchallenge.appointment_service.core.dto.AuthResponse;
import com.fiap.techchallenge.appointment_service.core.dto.LoginRequest;
import com.fiap.techchallenge.appointment_service.core.dto.UserDetailsImpl;
import com.fiap.techchallenge.appointment_service.core.dto.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fiap.techchallenge.appointment_service.core.exception.InvalidCredentialsException;
import com.fiap.techchallenge.appointment_service.core.exception.OrchestratorUnavailableException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutorizacaoService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthResponse authenticate(LoginRequest loginRequest) {
        log.debug("Iniciando autenticação para usuário: {}", loginRequest.getLogin());

        java.util.Map<String, Object> tokenResponse;
        try {
            java.util.Map<String, Object> credentials = new java.util.HashMap<>();
            credentials.put("login", loginRequest.getLogin());
            credentials.put("senha", loginRequest.getSenha());
            if (loginRequest.getPerfil() != null && !loginRequest.getPerfil().isBlank()) {
                credentials.put("perfil", loginRequest.getPerfil());
            }
            tokenResponse = usuarioService.loginCredentials(credentials);
        } catch (RuntimeException e) {
            log.error("Erro ao chamar usuario-service para login do usuário '{}': {}", loginRequest.getLogin(),
                    e.getMessage(), e);
            throw new OrchestratorUnavailableException("Usuario service unavailable", e);
        }

        if (tokenResponse == null || !tokenResponse.containsKey("token")) {
            log.warn("Credenciais inválidas para usuário: {}", loginRequest.getLogin());
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        String token = String.valueOf(tokenResponse.get("token"));

        String perfilTipo = null;
        Long userId = null;
        try {
            perfilTipo = jwtService.getUserProfileFromToken(token);
        } catch (Exception e) {
            log.debug("Não foi possível extrair perfil do token: {}", e.getMessage());
        }
        try {
            Object userIdObj = tokenResponse.get("userId");
            if (userIdObj instanceof Number) {
                userId = ((Number) userIdObj).longValue();
            } else if (userIdObj instanceof String) {
                userId = Long.parseLong((String) userIdObj);
            }
        } catch (Exception e) {
            log.debug("Não foi possível extrair userId do response de login: {}", e.getMessage());
        }

        UsuarioResponse usuario = UsuarioResponse.builder()
                .id(userId)
                .login(loginRequest.getLogin())
                .nome(null)
                .email(null)
                .ativo(true)
                .perfil(perfilTipo != null
                        ? com.fiap.techchallenge.appointment_service.core.dto.PerfilResponse.builder().tipo(perfilTipo)
                                .build()
                        : null)
                .build();

        var userDetails = new UserDetailsImpl(usuario);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        java.time.LocalDateTime expiresAt = null;
        try {
            expiresAt = jwtService.getExpirationFromToken(token);
        } catch (Exception e) {
            log.debug("Não foi possível extrair expiration do token retornado pelo usuario-service: {}",
                    e.getMessage());
        }

        log.info("Autenticação delegada ao usuario-service bem-sucedida para usuário: {}", loginRequest.getLogin());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(usuario.getId())
                .login(usuario.getLogin())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil() != null ? usuario.getPerfil().getTipo() : null)
                .expiresAt(expiresAt)
                .build();
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public String getUsernameFromToken(String token) {
        return jwtService.getUsernameFromToken(token);
    }

}