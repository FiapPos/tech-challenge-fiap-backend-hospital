package com.fiap.techchallenge.orchestrator_service.client;
<<<<<<< HEAD

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-service", url = "${services.usuario.url}")
public interface UsuarioServiceClient {

    @GetMapping("/usuarios/existe/{usuarioId}")
    boolean existe(@PathVariable("usuarioId") Long usuarioId);

    @GetMapping("/usuarios/por-login/{login}")
    com.fiap.techchallenge.orchestrator_service.dto.UsuarioDto buscarPorLogin(@PathVariable("login") String login);
=======
import com.fiap.techchallenge.orchestrator_service.dto.EspecialidadeDTO;
import com.fiap.techchallenge.orchestrator_service.dto.UsuarioDTO;
import com.fiap.techchallenge.orchestrator_service.enums.Perfil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "usuario-service", url = "${services.usuario.url}")
public interface UsuarioServiceClient {
    
    @GetMapping("/api/usuarios/{usuarioId}")
    UsuarioDTO buscaPor(@PathVariable("usuarioId") Long usuarioId, @RequestParam Perfil perfil);

    @GetMapping("/api/usuarios/{usuarioId}")
    UsuarioDTO buscaPor(@PathVariable("usuarioId") Long usuarioId, @RequestParam Perfil perfil, @RequestParam Long especialidadeId);

    @GetMapping("/api/especialidades/{especialidadeId}")
    EspecialidadeDTO buscaPor(@PathVariable("especialidadeId") Long especialidadeId);
>>>>>>> origin/main
}