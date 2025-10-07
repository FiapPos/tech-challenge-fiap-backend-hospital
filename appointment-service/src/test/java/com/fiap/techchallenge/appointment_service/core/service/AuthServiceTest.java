package com.fiap.techchallenge.appointment_service.core.service;

import com.fiap.techchallenge.appointment_service.core.dto.request.LoginRequest;
import com.fiap.techchallenge.appointment_service.core.dto.response.UsuarioResponse;
import com.fiap.techchallenge.appointment_service.core.exception.InvalidCredentialsException;
import com.fiap.techchallenge.appointment_service.core.exception.OrchestratorUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    private UsuarioService usuarioService;
    private JwtService jwtService;

    private AutorizacaoService autorizacaoService;

    @BeforeEach
    void setup() {
        usuarioService = Mockito.mock(UsuarioService.class);
        jwtService = Mockito.mock(JwtService.class);
        autorizacaoService = new AutorizacaoService(usuarioService, jwtService);
    }

    @Test
    void autenticar_sucesso() throws Exception {
        var usuario = new UsuarioResponse();
        usuario.setId(1L);
        usuario.setLogin("user1");
        usuario.setNome("User One");
        usuario.setEmail("u1@example.com");
        usuario.setAtivo(true);

        java.util.Map<String, Object> tokenMap = new java.util.HashMap<>();
        tokenMap.put("token", "token123");
        tokenMap.put("userId", 1L);
        when(usuarioService.loginCredentials(Mockito.anyMap())).thenReturn(tokenMap);
        when(jwtService.getUserProfileFromToken("token123")).thenReturn("ADMIN");
        when(jwtService.getExpirationFromToken("token123")).thenReturn(java.time.LocalDateTime.now().plusHours(1));

        var req = new LoginRequest();
        req.setLogin("user1");
        req.setSenha("pwd");

        String token = autorizacaoService.authenticate(req);
        assertNotNull(token);
        assertEquals("token123", token);
    }

    @Test
    void autenticar_credenciaisInvalidas_lanca() throws Exception {
        var usuario = new UsuarioResponse();
        usuario.setId(1L);
        usuario.setLogin("user1");
        usuario.setAtivo(true);

        when(usuarioService.loginCredentials(Mockito.anyMap())).thenReturn(null);

        var req = new LoginRequest();
        req.setLogin("user1");
        req.setSenha("wrong");

        assertThrows(InvalidCredentialsException.class, () -> autorizacaoService.authenticate(req));
    }

    @Test
    void autenticar_orquestradorIndisponivel_lanca() throws Exception {
        when(usuarioService.loginCredentials(Mockito.anyMap())).thenThrow(new RuntimeException("conn fail"));

        var req = new LoginRequest();
        req.setLogin("user1");
        req.setSenha("pwd");

        assertThrows(OrchestratorUnavailableException.class, () -> autorizacaoService.authenticate(req));
    }
}
