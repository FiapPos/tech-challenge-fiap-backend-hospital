package com.fiap.techchallenge.agendamento_service.core.controller;

import com.fiap.techchallenge.agendamento_service.core.dto.Agendamento;
import com.fiap.techchallenge.agendamento_service.core.producer.KafkaProducerTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@RestController
public class AgendamentoTestController {

    private final KafkaProducerTest kafkaProducer;

    public AgendamentoTestController(KafkaProducerTest kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/api/agendamento/criacao")
    public ResponseEntity<ResponseStatus> create() {
        Agendamento agendamentoCriado = new Agendamento(
                "AGD-001",
                "João A.",
                LocalDateTime.of(2025, 10, 18, 8, 8),
                "Ortopedia",
                BigDecimal.valueOf(3500.00)
        );
        kafkaProducer.sendAgendamento(agendamentoCriado);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/agendamento/edicao")
    public ResponseEntity<ResponseStatus> edit() {
        Agendamento agendamento = new Agendamento(
                "AGD-001",
                "João A.",
                LocalDateTime.of(2025, 10, 18, 8, 8),
                "Ortopedia",
                BigDecimal.valueOf(3500.00)
        );
        agendamento.setAtualizadoEm(now());
        kafkaProducer.sendAgendamento(agendamento);
        return ResponseEntity.ok().build();
    }

}
