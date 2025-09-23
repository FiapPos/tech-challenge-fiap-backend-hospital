package com.fiap.techchallenge.notificacao_service.configuration.kafka;

import com.fiap.techchallenge.notificacao_service.core.dto.AgendamentoCriadoEvento;
import com.fiap.techchallenge.notificacao_service.core.dto.AgendamentoEditadoEvento;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

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

    @Value("${spring.kafka.topic.agendamento-criado}")
    private String topicoAgendamentoCriado;

    @Value("${spring.kafka.topic.agendamento-editado}")
    private String topicoAgendamentoEditado;

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
    public ConsumerFactory<String, AgendamentoCriadoEvento> agendamentoCriadoConsumerFactory() {
        Map<String, Object> props = baseConsumerConfigs();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, AgendamentoCriadoEvento.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, AgendamentoEditadoEvento> agendamentoEditadoConsumerFactory() {
        Map<String, Object> props = baseConsumerConfigs();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, AgendamentoEditadoEvento.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AgendamentoCriadoEvento> agendamentoCriadoKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AgendamentoCriadoEvento> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(agendamentoCriadoConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AgendamentoEditadoEvento> agendamentoEditadoKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AgendamentoEditadoEvento> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(agendamentoEditadoConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
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

    @Bean
    public NewTopic criaTopicoDeOrquestrador() {
        return buildTopic(topicoOrquestrador);
    }
}
