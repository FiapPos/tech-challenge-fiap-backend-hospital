package com.fiap.techchallenge.appointment_service.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HistoricoRestTemplateConfig {

    @Bean
    public RestTemplate historicoRestTemplate(@Value("${historico-service.connect-timeout:2000}") int connectTimeout,
            @Value("${historico-service.read-timeout:5000}") int readTimeout) {
        SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(connectTimeout);
        f.setReadTimeout(readTimeout);
        return new RestTemplate(f);
    }
}
