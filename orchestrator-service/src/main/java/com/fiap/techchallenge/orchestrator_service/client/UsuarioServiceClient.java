package com.fiap.techchallenge.orchestrator_service.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-service", url = "${services.usuario.url}")
public interface UsuarioServiceClient {
    
    @GetMapping("/api/usuarios/existe/{usuarioId}")
    boolean existe(@PathVariable("usuarioId") Long usuarioId);
}