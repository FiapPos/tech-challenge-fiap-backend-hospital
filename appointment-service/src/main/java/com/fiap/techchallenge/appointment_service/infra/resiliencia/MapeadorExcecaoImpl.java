package com.fiap.techchallenge.appointment_service.infra.resiliencia;

import com.fiap.techchallenge.appointment_service.core.port.MapeadorExcecao;
import com.fiap.techchallenge.appointment_service.infra.web.MapeadorExcecaoChamadaRemota;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class MapeadorExcecaoImpl implements MapeadorExcecao {

    @Override
    public <T> Supplier<T> envolver(Supplier<T> supplier) {
        return MapeadorExcecaoChamadaRemota.envolver(supplier);
    }
}
