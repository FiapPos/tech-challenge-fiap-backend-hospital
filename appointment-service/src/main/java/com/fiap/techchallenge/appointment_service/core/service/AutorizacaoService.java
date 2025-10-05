package com.fiap.techchallenge.appointment_service.core.service;

import com.fiap.techchallenge.appointment_service.core.dto.request.LoginRequest;
import com.fiap.techchallenge.appointment_service.core.dto.response.UsuarioResponse;
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

    public String authenticate(LoginRequest loginRequest) {
        log.debug("Iniciando autenticação para usuário: {}", loginRequest.getLogin());

        java.util.Map<String, Object> tokenResponse = chamarUsuarioService(loginRequest);

        if (tokenResponse == null || !tokenResponse.containsKey("token")) {
            log.warn("Credenciais inválidas para usuário: {}", loginRequest.getLogin());
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        String token = String.valueOf(tokenResponse.get("token"));

        String perfilTipo = extrairPerfilDoToken(token);
        Long userId = extrairUserIdDoResponse(tokenResponse);

        UsuarioResponse usuario = buildUsuarioResponse(loginRequest.getLogin(), perfilTipo, userId);

        setAuthenticationContext(usuario);

        log.info("Autenticação delegada ao usuario-service bem-sucedida para usuário: {}", loginRequest.getLogin());

        return token;
    }

    private java.util.Map<String, Object> chamarUsuarioService(LoginRequest loginRequest) {
        try {
            return usuarioService.loginCredentials(buildCredentials(loginRequest));
        } catch (RuntimeException e) {
            log.error("Erro ao chamar usuario-service para login do usuário '{}': {}", loginRequest.getLogin(),
                    e.getMessage(), e);
            throw new OrchestratorUnavailableException("Usuario service unavailable", e);
        }
    }

    private java.util.Map<String, Object> buildCredentials(LoginRequest loginRequest) {
        var credentials = new java.util.HashMap<String, Object>();
        credentials.put("login", loginRequest.getLogin());
        credentials.put("senha", loginRequest.getSenha());

        String perfil = loginRequest.getPerfil();
        if (perfil != null && !perfil.isBlank()) {
            credentials.put("perfil", perfil.trim());
        }

        return credentials;
    }

    private String extrairPerfilDoToken(String token) {
        try {
            return jwtService.getUserProfileFromToken(token);
        } catch (Exception e) {
            log.debug("Não foi possível extrair perfil do token: {}", e.getMessage());
            return null;
        }
    }

    private Long extrairUserIdDoResponse(java.util.Map<String, Object> tokenResponse) {
        try {
            Object userIdObj = tokenResponse.get("userId");
            if (userIdObj instanceof Number) {
                return ((Number) userIdObj).longValue();
            } else if (userIdObj instanceof String) {
                return Long.parseLong((String) userIdObj);
            }
        } catch (Exception e) {
            log.debug("Não foi possível extrair userId do response de login: {}", e.getMessage());
        }
        return null;
    }

    private UsuarioResponse buildUsuarioResponse(String login, String perfilTipo, Long userId) {
        return UsuarioResponse.builder()
                .id(userId)
                .login(login)
                .perfil(perfilTipo != null
                        ? com.fiap.techchallenge.appointment_service.core.dto.response.PerfilResponse.builder()
                                .tipo(perfilTipo)
                                .build()
                        : null)
                .build();
    }

    private void setAuthenticationContext(UsuarioResponse usuario) {
        var userDetails = new UserDetailsImpl(usuario);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public String getUsernameFromToken(String token) {
        return jwtService.getUsernameFromToken(token);
    }

    public String authenticateToken(LoginRequest loginRequest) {

        return authenticate(loginRequest);
    }

}