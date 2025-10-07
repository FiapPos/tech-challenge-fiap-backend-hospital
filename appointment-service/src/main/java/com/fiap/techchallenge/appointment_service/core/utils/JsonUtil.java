package com.fiap.techchallenge.appointment_service.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class JsonUtil {

    private final ObjectMapper objectMapper;

    public JsonUtil() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            log.error("Erro ao converter objeto para JSON: {}", ex.getMessage(), ex);
            return null;
        }
    }

    public JsonNode toAuthEvent(String json) {
        try {

            JsonNode node = objectMapper.readTree(json);
            if (node != null && node.isObject()) {
                ObjectNode obj = (ObjectNode) node;
                JsonNode sourceNode = obj.get("source");
                String sourceText = sourceNode != null && !sourceNode.isNull() ? sourceNode.asText(null) : null;
                // Keep the original source text (normalization could be added here)
                String resolvedName = sourceText;
                obj.set("source", TextNode.valueOf(resolvedName != null ? resolvedName : "ORCHESTRATOR_SERVICE"));
                return obj;
            }
            return objectMapper.readTree(json);
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            log.error("Erro ao converter JSON para AuthEvent JsonNode: {}", ex.getMessage(), ex);
            return null;
        }
    }

    public <T> T toObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            log.error("Erro ao converter JSON para {}: {}", clazz.getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }
}