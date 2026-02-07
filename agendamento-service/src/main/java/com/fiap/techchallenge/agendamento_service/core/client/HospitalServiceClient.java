package com.fiap.techchallenge.agendamento_service.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "hospital-service", url = "${services.hospital.url:http://hospital-service:3000}")
public interface HospitalServiceClient {

    @GetMapping("/api/hospitais/{id}")
    Map<String, Object> buscarHospitalPorId(@PathVariable("id") Long id);
}
