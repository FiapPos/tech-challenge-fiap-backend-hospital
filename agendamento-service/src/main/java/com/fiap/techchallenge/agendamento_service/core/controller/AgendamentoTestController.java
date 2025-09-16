package com.fiap.techchallenge.agendamento_service.core.controller;

import com.fiap.techchallenge.agendamento_service.core.dto.AgendamentoCriado;
import com.fiap.techchallenge.agendamento_service.core.dto.AgendamentoEditado;
import com.fiap.techchallenge.agendamento_service.core.producer.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
public class AgendamentoTestController {

    private final KafkaProducer kafkaProducer;

    public AgendamentoTestController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/api/agendamento/criacao")
    public ResponseEntity<ResponseStatus> create() {
        AgendamentoCriado agendamentoCriado = new AgendamentoCriado(
                "AGD-001",
                "João A.",
                LocalDateTime.of(2025, 10, 18, 8, 8),
                "Ortopedia",
                BigDecimal.valueOf(3500.00)
        );
        kafkaProducer.sendAgendamentoCriado(agendamentoCriado);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/agendamento/edicao")
    public ResponseEntity<ResponseStatus> edicao() {
        AgendamentoEditado agendamentoEditado = new AgendamentoEditado(
                "AGD-001",
                "João A.",
                LocalDateTime.of(2025, 10, 18, 8, 8),
                "Ortopedia",
                BigDecimal.valueOf(3500.00)
        );
        kafkaProducer.sendAgendamentoEditado(agendamentoEditado);
        return ResponseEntity.ok().build();
    }

}
