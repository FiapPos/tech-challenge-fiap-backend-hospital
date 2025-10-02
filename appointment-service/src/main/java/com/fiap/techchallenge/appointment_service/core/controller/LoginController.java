package com.fiap.techchallenge.appointment_service.core.controller;

import com.fiap.techchallenge.appointment_service.core.dto.LoginRequest;
import com.fiap.techchallenge.appointment_service.core.service.AutorizacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AutorizacaoService autorizacaoService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Tentativa de login para usu\u00e1rio: {}", loginRequest.getLogin());
        String token = autorizacaoService.authenticate(loginRequest).getToken();
        log.info("Login realizado com sucesso para usu\u00e1rio: {}", loginRequest.getLogin());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API Gateway is running");
    }
}
