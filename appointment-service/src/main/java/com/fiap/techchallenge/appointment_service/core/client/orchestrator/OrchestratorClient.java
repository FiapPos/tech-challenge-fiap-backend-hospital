package com.fiap.techchallenge.appointment_service.core.client.orchestrator;

import com.fiap.techchallenge.appointment_service.core.facade.ChamadaRemotaFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

@Component
public class OrchestratorClient {

    private final OrchestratorFeignClient feign;
    private final ChamadaRemotaFacade chamadaRemotaFacade;

    @Autowired
    public OrchestratorClient(OrchestratorFeignClient feign, ChamadaRemotaFacade chamadaRemotaFacade) {
        this.feign = feign;
        this.chamadaRemotaFacade = chamadaRemotaFacade;
    }

    public ResponseEntity<Object> createAgendamento(Object payload) {
        Supplier<ResponseEntity<Object>> call = () -> feign.createAgendamento(payload);
        return chamadaRemotaFacade.invocarParaResposta("createAgendamento", call);
    }

    public ResponseEntity<Object> updateAgendamento(String id, Object payload) {
        Supplier<ResponseEntity<Object>> call = () -> feign.updateAgendamento(id, payload);
        return chamadaRemotaFacade.invocarParaResposta("updateAgendamento", call);
    }
}
