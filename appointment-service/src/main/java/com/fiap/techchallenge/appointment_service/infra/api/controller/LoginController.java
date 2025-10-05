package com.fiap.techchallenge.appointment_service.infra.api.controller;

import com.fiap.techchallenge.appointment_service.core.dto.request.LoginRequest;
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

        String token = autorizacaoService.authenticateToken(loginRequest);

        return ResponseEntity.ok(Map.of("token", token));
    }
}
