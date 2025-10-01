package com.fiap.techchallenge.appointment_service.core.service;

import com.fiap.techchallenge.appointment_service.core.document.AuthEvent;
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
    private final EventosAtenticacao eventosAutenticacao;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public AuthResponse authenticate(LoginRequest loginRequest) {
        log.debug("Iniciando autenticação para usuário: {}", loginRequest.getLogin());

        UsuarioResponse usuario;
        try {
            usuario = usuarioService.findUsuarioByLogin(loginRequest.getLogin());
        } catch (RuntimeException e) {
            log.error("Erro ao consultar orquestrador para usuário '{}': {}", loginRequest.getLogin(), e.getMessage(),
                    e);
            throw new OrchestratorUnavailableException("Orchestrator unavailable", e);
        }

        if (usuario == null || !usuario.isAtivo()) {
            log.warn("Usuário inativo ou não encontrado: {}", loginRequest.getLogin());
            throw new InvalidCredentialsException("Usuário inativo ou não encontrado");
        }

        // 2. Validar credenciais localmente (o orchestrator apenas fornece os dados do
        // usuário)
        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            log.warn("Senha não encontrada no orquestrador para usuário: {}", loginRequest.getLogin());
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        boolean credenciaisValidas = passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha());
        if (!credenciaisValidas) {
            log.warn("Credenciais inválidas para usuário: {}", loginRequest.getLogin());
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        // 3. Criar authentication e contexto de segurança
        var userDetails = new UserDetailsImpl(usuario);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4. Gerar token JWT
        String token = jwtService.generateToken(authentication);

        String perfilTipo = usuario.getPerfil() != null ? usuario.getPerfil().getTipo() : null;

        log.info("Autenticação bem-sucedida para usuário: {}", loginRequest.getLogin());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(usuario.getId())
                .login(usuario.getLogin())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(perfilTipo)
                .expiresAt(jwtService.getExpirationFromToken(token))
                .build();
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public String getUsernameFromToken(String token) {
        return jwtService.getUsernameFromToken(token);
    }

    public void processAuthenticationEvent(AuthEvent authEvent) {
        eventosAutenticacao.processarEventoAutenticacao(authEvent);
    }

    public void processAuthorizationEvent(AuthEvent authEvent) {
        eventosAutenticacao.processarEventoAutorizacao(authEvent);
    }

    public void processGeneralAuthEvent(AuthEvent authEvent) {
        eventosAutenticacao.processarEventoGeralAuth(authEvent);
    }
}