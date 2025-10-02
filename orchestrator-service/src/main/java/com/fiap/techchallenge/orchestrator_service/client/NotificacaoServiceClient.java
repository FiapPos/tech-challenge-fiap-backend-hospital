package com.fiap.techchallenge.orchestrator_service.client;

import com.fiap.techchallenge.orchestrator_service.dto.NotificacaoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao-service", url = "${services.notificacao.url}")
public interface NotificacaoServiceClient {

    @PostMapping("/api/notificacoes/enviar")
    void enviarNotificacao(@RequestBody NotificacaoDTO notificacao);
}