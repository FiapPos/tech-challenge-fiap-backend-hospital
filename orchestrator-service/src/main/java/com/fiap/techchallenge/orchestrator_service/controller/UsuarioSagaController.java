package com.fiap.techchallenge.orchestrator_service.controller;

import com.fiap.techchallenge.orchestrator_service.client.UsuarioServiceClient;
import com.fiap.techchallenge.orchestrator_service.dto.UsuarioDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/saga/usuario")
@RequiredArgsConstructor
public class UsuarioSagaController {

    private final UsuarioServiceClient usuarioServiceClient;

    @GetMapping("/login/{login}")
    public ResponseEntity<UsuarioDto> buscarUsuarioPorLoginParaSaga(@PathVariable String login) {
        UsuarioDto usuario = usuarioServiceClient.buscarPorLogin(login);
        return ResponseEntity.ok(usuario);
    }
}
