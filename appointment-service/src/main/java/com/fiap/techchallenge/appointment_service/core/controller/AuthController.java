package com.fiap.techchallenge.appointment_service.core.controller;

import com.fiap.techchallenge.appointment_service.core.dto.AuthResponse;
import com.fiap.techchallenge.appointment_service.core.dto.LoginRequest;
import com.fiap.techchallenge.appointment_service.core.service.AutorizacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutorizacaoService autorizacaoService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Tentativa de login para usuário: {}", loginRequest.getLogin());
        AuthResponse response = autorizacaoService.authenticate(loginRequest);
        log.info("Login realizado com sucesso para usuário: {}", loginRequest.getLogin());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        log.debug("Validando token JWT");

        boolean isValid = autorizacaoService.validateToken(token);
        log.debug("Resultado da validação do token: {}", isValid);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API Gateway is running");
    }
}
