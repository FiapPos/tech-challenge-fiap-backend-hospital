package com.fiap.techchallenge.appointment_service.core.facade;

import com.fiap.techchallenge.appointment_service.core.port.MapeadorExcecao;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class ChamadaRemotaFacade {

    private final MapeadorExcecao mapeadorExcecao;

    public ChamadaRemotaFacade(MapeadorExcecao mapeadorExcecao) {
        this.mapeadorExcecao = mapeadorExcecao;
    }

    public ResponseEntity<Object> invocarParaResposta(String nome,
            Supplier<ResponseEntity<Object>> chamadaRest) {

        Supplier<ResponseEntity<Object>> supplierTratado = mapeadorExcecao.envolver(chamadaRest);
        return supplierTratado.get();
    }
}
