package com.fiap.techchallenge.feedback.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic feedbackCriadoTopic() {
        return TopicBuilder.name("feedback.criado")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic feedbackCriticoTopic() {
        return TopicBuilder.name("feedback.critico")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic feedbackRespondidoTopic() {
        return TopicBuilder.name("feedback.respondido")
                .partitions(3)
                .replicas(1)
                .build();
    }
}

