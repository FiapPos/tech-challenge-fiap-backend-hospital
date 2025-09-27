package com.fiap.techchallenge.agendamento_service.core.dto;

import com.fiap.techchallenge.agendamento_service.core.enums.ESagaStatus;

import java.io.Serializable;

import static com.fiap.techchallenge.agendamento_service.core.enums.ESagaStatus.FAIL;
import static com.fiap.techchallenge.agendamento_service.core.enums.ESagaStatus.SUCCESS;

public class EventoOrquestrador implements Serializable {

    private String fonte = "AGENDAMENTO_SERVICE";

    private ESagaStatus status;

    public EventoOrquestrador(ESagaStatus status) {
        this.status = status;
    }

    public static EventoOrquestrador constroiEventoSucesso() {
        return new EventoOrquestrador(SUCCESS);
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public ESagaStatus getStatus() {
        return status;
    }

    public void setStatus(ESagaStatus status) {
        this.status = status;
    }

    public static EventoOrquestrador constroiEventoFalha() {
        return new EventoOrquestrador(FAIL);
    }

    @Override
    public String toString() {
        return "{" +
                "fonte='" + fonte + '\'' +
                ", status=" + status +
                '}';
    }

}
