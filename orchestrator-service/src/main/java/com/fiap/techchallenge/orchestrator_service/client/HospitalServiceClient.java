package com.fiap.techchallenge.orchestrator_service.client;

import com.fiap.techchallenge.orchestrator_service.dto.HospitalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hospital-service", url = "${services.hospital.url}")
public interface HospitalServiceClient {

    @GetMapping("/api/hospitais/{hospitalId}")
    HospitalDTO buscaPor(@PathVariable("hospitalId") Long hospitalId);
}
