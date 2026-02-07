package com.fiap.techchallenge.agendamento_service.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "usuario-service", url = "${services.usuario.url:http://usuario-service:3000}")
public interface UsuarioServiceClient {

    @GetMapping("/api/usuarios/{id}")
    Map<String, Object> buscarUsuarioPorId(@PathVariable("id") Long id, @RequestParam("perfil") String perfil);

    @GetMapping("/api/usuarios")
    Object listarUsuarios();

    @GetMapping("/api/especialidades/{id}")
    Map<String, Object> buscarEspecialidadePorId(@PathVariable("id") Long id);
}
