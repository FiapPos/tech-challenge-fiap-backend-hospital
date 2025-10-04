package com.fiap.techchallenge.appointment_service.core.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Component
public class UsuarioClient {

    @Value("${usuario-service.url:http://usuario-service:3000}")
    private String usuarioServiceBaseUrl;

    private final RestTemplate restTemplate;

    public UsuarioClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Object> loginOnUsuarioService(Object payload) {
        String url = usuarioServiceBaseUrl + "/login";
        Supplier<ResponseEntity<Object>> call = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        };

        return invokeForResponse(call);
    }

    private ResponseEntity<Object> invokeForResponse(Supplier<ResponseEntity<Object>> restCall) {
        try {
            return restCall.get();
        } catch (RestClientResponseException ex) {
            org.springframework.http.HttpStatusCode statusCode = ex.getStatusCode();
            org.springframework.http.HttpStatus status = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
            if (statusCode != null) {
                try {
                    status = org.springframework.http.HttpStatus.valueOf(statusCode.value());
                } catch (IllegalArgumentException ignored) {
                    status = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
                }
            }
            String body = ex.getResponseBodyAsString();
            throw new OrchestratorException(status, body == null ? "" : body);
        }
    }
}
