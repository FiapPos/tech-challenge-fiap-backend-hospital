package com.fiap.techchallenge.appointment_service.core.client.usuario;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "usuario-service", url = "${usuario-service.url:http://usuario-service:3000}")
public interface UsuarioFeignClient {

    @PostMapping(value = "/api/auth/login")
    Map<String, Object> login(@RequestBody Map<String, Object> payload);

}
