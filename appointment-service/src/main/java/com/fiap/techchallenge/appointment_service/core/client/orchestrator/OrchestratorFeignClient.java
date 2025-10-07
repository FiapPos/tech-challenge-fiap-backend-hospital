package com.fiap.techchallenge.appointment_service.core.client.orchestrator;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "orchestrator-service", url = "${orchestrator-service.url:http://orchestrator-service:8080}")
public interface OrchestratorFeignClient {

    @PostMapping(value = "/api/saga/agendamentos")
    ResponseEntity<Object> createAgendamento(@RequestBody Object payload);

    @PutMapping(value = "/api/saga/agendamentos/{id}")
    ResponseEntity<Object> updateAgendamento(@PathVariable("id") String id, @RequestBody Object payload);

}
