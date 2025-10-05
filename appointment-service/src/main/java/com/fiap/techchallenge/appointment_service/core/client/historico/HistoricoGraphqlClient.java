package com.fiap.techchallenge.appointment_service.core.client.historico;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HistoricoGraphqlClient {

    private final RestTemplate historicoRestTemplate;
    private final ObjectMapper objectMapper;

    public JsonNode execute(String query, Map<String, Object> variables, String baseUrl) {
        try {
            Map<String, Object> body = Map.of("query", query, "variables", variables == null ? Map.of() : variables);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            String url = baseUrl.endsWith("/") ? baseUrl + "graphql" : baseUrl + "/graphql";

            String resp = historicoRestTemplate.postForObject(url, entity, String.class);
            JsonNode root = objectMapper.readTree(resp);
            return root;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao chamar historico-service GraphQL: " + e.getMessage(), e);
        }
    }
}
