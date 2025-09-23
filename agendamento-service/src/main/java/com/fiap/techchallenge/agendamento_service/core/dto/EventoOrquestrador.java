package com.fiap.techchallenge.agendamento_service.core.dto;

import com.fiap.techchallenge.agendamento_service.core.enums.ESagaStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import static com.fiap.techchallenge.agendamento_service.core.enums.ESagaStatus.FAIL;
import static com.fiap.techchallenge.agendamento_service.core.enums.ESagaStatus.SUCCESS;

@Setter
@Getter
public class EventoOrquestrador implements Serializable {

    private String fonte = "AGENDAMENTO_SERVICE";

    private ESagaStatus status;

    public EventoOrquestrador(ESagaStatus status) {
        this.status = status;
    }

    public static EventoOrquestrador constroiEventoSucesso() {
        return new EventoOrquestrador(SUCCESS);
    }


    public static EventoOrquestrador constroiEventoFalha() {
        return new EventoOrquestrador(FAIL);
    }

}
