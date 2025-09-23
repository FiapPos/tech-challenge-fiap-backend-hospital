package com.fiap.techchallenge.notificacao_service.core.dto;

import com.fiap.techchallenge.notificacao_service.core.enums.ESagaStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import static com.fiap.techchallenge.notificacao_service.core.enums.ESagaStatus.*;

@Setter
@Getter
public class EventoOrquestrador implements Serializable {

    private String fonte = "NOTIFICACAO_SERVICE";

    private ESagaStatus status;

    public EventoOrquestrador(ESagaStatus status) {
        this.status = status;
    }

    public static EventoOrquestrador constroiEventoSucesso() {
        return new EventoOrquestrador(SUCCESS);
    }
}
