package com.fiap.techchallenge.agendamento_service.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "usuario-service", url = "${services.usuario.url:http://usuario-service:3000}")
public interface UsuarioServiceClient {

    @GetMapping("/api/usuarios/{id}")
    Map<String, Object> buscarUsuarioPorId(@PathVariable("id") Long id, @RequestParam("perfil") String perfil);

    @GetMapping("/api/usuarios")
    Object listarUsuarios();

    @GetMapping("/api/especialidades/{id}")
    Map<String, Object> buscarEspecialidadePorId(@PathVariable("id") Long id);

    @PutMapping("/api/usuarios/atualiza-chat-id/{id}")
    void vincularChatId(@PathVariable Long id, @RequestParam("chatId") long chatId);
}
