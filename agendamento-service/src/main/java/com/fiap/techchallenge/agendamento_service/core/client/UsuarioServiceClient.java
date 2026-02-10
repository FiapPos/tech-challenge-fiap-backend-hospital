package com.fiap.techchallenge.agendamento_service.core.client;

import com.fiap.techchallenge.agendamento_service.core.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "usuario-service", url = "${services.usuario.url:http://usuario-service:3000}")
public interface UsuarioServiceClient {

    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO buscarUsuarioPorId(@PathVariable("id") Long id, @RequestParam("perfil") String perfil);

    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO buscarUsuarioPorId(@PathVariable("id") Long id,
                                  @RequestParam("perfil") String perfil,
                                  @RequestParam("especialidadeId") Long especialidadeId);

    @GetMapping("/api/usuarios")
    Object listarUsuarios();

    @GetMapping("/api/especialidades/{id}")
    Object buscarEspecialidadePorId(@PathVariable("id") Long id);

    @PutMapping("/api/usuarios/atualiza-chat-id/{id}")
    void vincularChatId(@PathVariable Long id, @RequestParam("chatId") long chatId);

    @GetMapping("/api/usuarios/por-chat/{chatId}")
    Long buscarUsuarioPorChatId(@PathVariable("chatId") long chatId);
}
