package com.fiap.techchallenge.orchestrator_service.client;

import com.fiap.techchallenge.orchestrator_service.dto.EspecialidadeDTO;
import com.fiap.techchallenge.orchestrator_service.dto.UsuarioDto;
import com.fiap.techchallenge.orchestrator_service.enums.Perfil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "usuario-service", url = "${services.usuario.url}")
public interface UsuarioServiceClient {

    @GetMapping("/api/usuarios/{usuarioId}")
    UsuarioDto buscaPor(@PathVariable("usuarioId") Long usuarioId, @RequestParam Perfil perfil);

    @GetMapping("/api/usuarios/{usuarioId}")
    UsuarioDto buscaPor(@PathVariable("usuarioId") Long usuarioId, @RequestParam Perfil perfil, @RequestParam Long especialidadeId);

    @GetMapping("/api/especialidades/{especialidadeId}")
    EspecialidadeDTO buscaPor(@PathVariable("especialidadeId") Long especialidadeId);
}