package com.fiap.techchallenge.agendamento_service.configuration.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfig {

    private static final Integer CONTAGEM_PARTICAO = 1;
    private static final Integer CONTAGEM_REPLICA = 1;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topic.agendamento-criado}")
    private String topicoAgendamentoCriado;

    @Value("${spring.kafka.topic.agendamento-editado}")
    private String topicoAgendamentoEditado;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    private NewTopic buildTopic(String name) {
        return TopicBuilder
                .name(name)
                .replicas(CONTAGEM_PARTICAO)
                .partitions(CONTAGEM_REPLICA)
                .build();

    }

    @Bean
    public NewTopic criaTopicoDeAgendamentoCriado() {
        return buildTopic(topicoAgendamentoCriado);
    }

    @Bean
    public NewTopic criaTopicoDeAgendamentoEditado() {
        return buildTopic(topicoAgendamentoEditado);
    }
}
