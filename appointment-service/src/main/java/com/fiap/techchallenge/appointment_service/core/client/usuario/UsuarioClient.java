package com.fiap.techchallenge.appointment_service.core.client.usuario;

import com.fiap.techchallenge.appointment_service.core.facade.ChamadaRemotaFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class UsuarioClient {

    private final UsuarioFeignClient feign;
    private final ChamadaRemotaFacade chamadaRemotaFacade;

    public UsuarioClient(UsuarioFeignClient feign, ChamadaRemotaFacade chamadaRemotaFacade) {
        this.feign = feign;
        this.chamadaRemotaFacade = chamadaRemotaFacade;
    }

    public ResponseEntity<Object> loginOnUsuarioService(Object payload) {
        Supplier<ResponseEntity<Object>> call = () -> {
            @SuppressWarnings("unchecked")
            Map<String, Object> res = feign.login((Map<String, Object>) payload);
            return ResponseEntity.ok(res);
        };

        return chamadaRemotaFacade.invocarParaResposta("loginUsuarioService", call);
    }
}