package com.fiap.techchallenge.historico_service.configuration.kafka;

import com.fiap.techchallenge.historico_service.core.dto.Evento;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
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

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffSetReset;

    @Value("${spring.kafka.topic.historico-sucesso}")
    private String topicoHistoricoSucesso;

    @Value("${spring.kafka.topic.historico-falha}")
    private String topicoHistoricoFalha;

    @Value("${spring.kafka.topic.orquestrador}")
    private String topicoOrquestrador;

    private Map<String, Object> baseConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffSetReset);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, true);
        return props;
    }

    @Bean
    public ConsumerFactory<String, Evento> appointmentConsumerFactory() {
        Map<String, Object> props = baseConsumerConfigs();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Evento.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Evento> appointmentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Evento> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(appointmentConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

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
    public NewTopic criaTopicoDeHistoricoSucesso() {
        return buildTopic(topicoHistoricoSucesso);
    }

    @Bean
    public NewTopic criaTopicoDeHistoricoFail() {
        return buildTopic(topicoHistoricoFalha);
    }

    @Bean
    public NewTopic criaTopicoDeOrquestrador() {
        return buildTopic(topicoOrquestrador);
    }
}
