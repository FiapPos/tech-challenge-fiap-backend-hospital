package com.fiap.techchallenge.appointment_service.core.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fiap.techchallenge.appointment_service.core.dto.AgendamentoRequest;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    private final ObjectMapper objectMapper;

    public AgendamentoMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectNode toSagaPayload(AgendamentoRequest req) {
        ObjectNode node = objectMapper.createObjectNode();
        if (req.getPacienteId() != null)
            node.put("pacienteId", req.getPacienteId());
        if (req.getMedicoId() != null)
            node.put("medicoId", req.getMedicoId());
        node.putPOJO("dataHora", req.getDataHora());
        return node;
    }
}
