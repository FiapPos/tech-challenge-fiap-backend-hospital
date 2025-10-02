package com.fiap.techchallenge.appointment_service.core.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import java.util.function.Supplier;

@Component
public class OrchestratorClient {

    @Value("${orchestrator-service.url:http://orchestrator-service:8080}")
    private String orchestratorBaseUrl;

    @Value("${usuario-service.url:http://usuario-service:3000}")
    private String usuarioServiceBaseUrl;

    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;
    private final RetryRegistry retryRegistry;

    public OrchestratorClient(RestTemplate restTemplate) {
        this(restTemplate, null, null);
    }

    public OrchestratorClient(RestTemplate restTemplate,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this(restTemplate, circuitBreakerFactory, null);
    }

    public OrchestratorClient(RestTemplate restTemplate,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory,
            RetryRegistry retryRegistry) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.retryRegistry = retryRegistry;
    }

    public ResponseEntity<Object> loginOnUsuarioService(Object payload) {
        String url = usuarioServiceBaseUrl + "/login";
        Supplier<ResponseEntity<Object>> call = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        };

        return invokeForResponse("loginUsuarioService", call);
    }

    public ResponseEntity<Object> createAgendamentoSaga(Object payload, HttpHeaders incomingHeaders) {
        String url = orchestratorBaseUrl + "/api/saga/agendamentos";
        Supplier<ResponseEntity<Object>> call = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            copyForwardingHeaders(incomingHeaders, headers);

            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        };

        return invokeForResponse("createAgendamentoSaga", call);
    }

    public ResponseEntity<Object> updateAgendamentoWithHeaders(Long id, Object payload, HttpHeaders incomingHeaders) {
        String url = orchestratorBaseUrl + "/api/agendamento/edicao/{id}";
        Supplier<ResponseEntity<Object>> call = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            copyForwardingHeaders(incomingHeaders, headers);

            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class, id);
        };

        return invokeForResponse("updateAgendamento", call);
    }

    private ResponseEntity<Object> invokeForResponse(String name, Supplier<ResponseEntity<Object>> restCall) {
        Supplier<ResponseEntity<Object>> supplier = () -> {
            try {
                return restCall.get();
            } catch (RestClientResponseException ex) {
                org.springframework.http.HttpStatusCode statusCode = ex.getStatusCode();
                HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                if (statusCode != null) {
                    try {
                        status = HttpStatus.valueOf(statusCode.value());
                    } catch (IllegalArgumentException ignored) {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                    }
                }
                String body = ex.getResponseBodyAsString();
                throw new OrchestratorException(status, body == null ? "" : body);
            }
        };

        return executeWithResilience(name, supplier);
    }

    private <T> T executeWithResilience(String name, Supplier<T> supplier) {
        Supplier<T> effectiveSupplier = supplier;

        if (this.retryRegistry != null) {
            Retry retry = this.retryRegistry.retry(name);
            effectiveSupplier = () -> Retry.decorateSupplier(retry, supplier).get();
        }

        if (this.circuitBreakerFactory == null) {
            return effectiveSupplier.get();
        }

        return this.circuitBreakerFactory.create(name).run(effectiveSupplier::get, throwable -> {

            if (throwable instanceof OrchestratorException) {
                throw (OrchestratorException) throwable;
            }

            throw new RuntimeException(throwable);
        });
    }

    private void copyForwardingHeaders(HttpHeaders source, HttpHeaders dest) {
        if (source == null || dest == null)
            return;
        if (source.containsKey("X-User-Profile"))
            dest.put("X-User-Profile", source.get("X-User-Profile"));
        if (source.containsKey("X-Username"))
            dest.put("X-Username", source.get("X-Username"));
        if (source.containsKey(HttpHeaders.AUTHORIZATION))
            dest.put(HttpHeaders.AUTHORIZATION, source.get(HttpHeaders.AUTHORIZATION));
    }

}