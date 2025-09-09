package com.fiap.techchallenge.notificacao_service.producer;

import com.fiap.techchallenge.notificacao_service.dto.NotificacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerApplication implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducerApplication.class);

    private final KafkaTemplate<String, NotificacaoService> kafkaTemplate;

    public KafkaProducerApplication(KafkaTemplate<String, NotificacaoService> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 10; i++) {
            NotificacaoService notificacaoService = new NotificacaoService(1L, "Notebook: " + i, 2, 3500.00);
            kafkaTemplate.send("notificacao", notificacaoService);
            logger.info("Notificação enviada com sucesso: {}", notificacaoService);
        }
    }
}
