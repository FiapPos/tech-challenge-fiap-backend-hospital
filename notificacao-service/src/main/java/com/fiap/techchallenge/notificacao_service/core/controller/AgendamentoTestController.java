package com.fiap.techchallenge.notificacao_service.core.controller;

import com.fiap.techchallenge.notificacao_service.core.dto.NotificacaoAgendamento;
import com.fiap.techchallenge.notificacao_service.core.producer.KafkaProducer;
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

    @GetMapping("/api/notificacao")
    public ResponseEntity<ResponseStatus> create() {
        NotificacaoAgendamento agendamento = new NotificacaoAgendamento(
                "João A.",
                LocalDateTime.of(2025, 10, 18, 8, 8),
                "Ortopedia",
                BigDecimal.valueOf(3500.00)
        );
        kafkaProducer.sendEvent(agendamento);
        return ResponseEntity.ok().build();
    }

}
