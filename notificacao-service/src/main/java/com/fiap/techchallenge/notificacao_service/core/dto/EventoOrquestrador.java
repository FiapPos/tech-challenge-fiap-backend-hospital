package com.fiap.techchallenge.notificacao_service.core.dto;

import com.fiap.techchallenge.notificacao_service.core.enums.ESagaStatus;

import java.io.Serializable;

public class EventoOrquestrador implements Serializable {

    private String fonte = "NOTIFICACAO_SERVICE";

    private ESagaStatus status;

    public EventoOrquestrador(ESagaStatus status) {
        this.status = status;
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
}
