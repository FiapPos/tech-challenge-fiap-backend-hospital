package com.fiap.techchallenge.agendamento_service.core.controller;

import com.fiap.techchallenge.agendamento_service.core.dto.DadosAgendamento;
import com.fiap.techchallenge.agendamento_service.core.dto.Evento;
import com.fiap.techchallenge.agendamento_service.core.producer.KafkaProducerTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
        DadosAgendamento dadosAgendamento = new DadosAgendamento(
                1L,
                1L,
                1L,
                1L,
                "Sonia A.",
                "Dr. Gustavo",
                "Hospital Stephanie Menezes",
                "Rua Julio Cesar, numero 10",
                "Ortopedia",
                LocalDateTime.of(2025, 10, 18, 8, 8),
                now());
        Evento evento = new Evento(dadosAgendamento);

        kafkaProducer.sendAgendamento(evento);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/agendamento/edicao")
    public ResponseEntity<ResponseStatus> edit() {
        DadosAgendamento dadosAgendamento = new DadosAgendamento(
                1L,
                1L,
                1L,
                1L,
                "Sonia A.",
                "Dr. Gustavo",
                "Hospital Stephanie Menezes",
                "Rua Julio Cesar, numero 10",
                "Ortopedia",
                LocalDateTime.of(2025, 10, 25, 8, 8),
                now());
        dadosAgendamento.setAtualizadoEm(now());
        Evento evento = new Evento(dadosAgendamento);

        kafkaProducer.sendAgendamento(evento);
        return ResponseEntity.ok().build();
    }

}
