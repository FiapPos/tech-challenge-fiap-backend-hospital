package com.fiap.techchallenge.appointment_service.core.service;

import com.fiap.techchallenge.appointment_service.core.service.UsuarioService;
import com.fiap.techchallenge.appointment_service.core.dto.AuthResponse;
import com.fiap.techchallenge.appointment_service.core.dto.LoginRequest;
import com.fiap.techchallenge.appointment_service.core.dto.UsuarioResponse;
import com.fiap.techchallenge.appointment_service.core.exception.InvalidCredentialsException;
import com.fiap.techchallenge.appointment_service.core.exception.OrchestratorUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.fiap.techchallenge.appointment_service.core.service.EventosAtenticacao;
import com.fiap.techchallenge.appointment_service.core.service.AutorizacaoService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    private UsuarioService usuarioService;
    private JwtService jwtService;
    private EventosAtenticacao eventosAtenticacao;
    private PasswordEncoder passwordEncoder;
    private AutorizacaoService autorizacaoService;

    @BeforeEach
    void setup() {
        usuarioService = Mockito.mock(UsuarioService.class);
        jwtService = Mockito.mock(JwtService.class);
        eventosAtenticacao = Mockito.mock(EventosAtenticacao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        // default behavior: matches returns true for convenience unless overridden
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        autorizacaoService = new AutorizacaoService(usuarioService, jwtService, eventosAtenticacao, passwordEncoder);
    }

    @Test
    void autenticar_sucesso() throws Exception {
        var usuario = new UsuarioResponse();
        usuario.setId(1L);
        usuario.setLogin("user1");
        usuario.setNome("User One");
        usuario.setEmail("u1@example.com");
        usuario.setAtivo(true);
        // profil handling omitted for brevity

        when(usuarioService.findUsuarioByLogin(anyString())).thenReturn(usuario);
        when(jwtService.generateToken(Mockito.any())).thenReturn("token123");
        when(jwtService.getExpirationFromToken("token123")).thenReturn(java.time.LocalDateTime.now().plusHours(1));

        var req = new LoginRequest();
        req.setLogin("user1");
        req.setSenha("pwd");

        AuthResponse res = autorizacaoService.authenticate(req);
        assertNotNull(res);
        assertEquals("user1", res.getLogin());
        assertEquals("token123", res.getToken());
    }

    @Test
    void autenticar_credenciaisInvalidas_lanca() throws Exception {
        var usuario = new UsuarioResponse();
        usuario.setId(1L);
        usuario.setLogin("user1");
        usuario.setAtivo(true);

        when(usuarioService.findUsuarioByLogin(anyString())).thenReturn(usuario);

        var req = new LoginRequest();
        req.setLogin("user1");
        req.setSenha("wrong");

        assertThrows(InvalidCredentialsException.class, () -> autorizacaoService.authenticate(req));
    }

    @Test
    void autenticar_orquestradorIndisponivel_lanca() throws Exception {
        when(usuarioService.findUsuarioByLogin(anyString())).thenThrow(new RuntimeException("conn fail"));

        var req = new LoginRequest();
        req.setLogin("user1");
        req.setSenha("pwd");

        assertThrows(OrchestratorUnavailableException.class, () -> autorizacaoService.authenticate(req));
    }
}
