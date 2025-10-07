package com.fiap.techchallenge.appointment_service.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() throws Exception {
        jwtService = new JwtService();
        // set secret to a sufficiently long key for HS256
        Field secretField = JwtService.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtService, "my-very-strong-secret-that-is-long-enough-to-be-secure-0123456789");

        Field expField = JwtService.class.getDeclaredField("jwtExpirationInSeconds");
        expField.setAccessible(true);
        expField.setInt(jwtService, 3600); // 1 hour
    }

    @Test
    void gerarToken_e_extrairPerfil_de_ROLE_authority() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "user1",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_MEDICO")));

        String token = jwtService.generateToken(auth);
        assertNotNull(token);

        assertTrue(jwtService.validateToken(token));
        String perfil = jwtService.getUserProfileFromToken(token);
        assertEquals("MEDICO", perfil);
    }

    @Test
    void gerarToken_e_extrairPerfil_de_PERFIL_authority() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "user2",
                null,
                List.of(new SimpleGrantedAuthority("PERFIL_ENFERMEIRO")));

        String token = jwtService.generateToken(auth);
        assertNotNull(token);

        assertTrue(jwtService.validateToken(token));
        String perfil = jwtService.getUserProfileFromToken(token);
        assertEquals("ENFERMEIRO", perfil);
    }
}
