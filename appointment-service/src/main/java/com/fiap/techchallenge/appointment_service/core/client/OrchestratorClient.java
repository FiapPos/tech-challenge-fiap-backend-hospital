package com.fiap.techchallenge.appointment_service.core.client;

import com.fiap.techchallenge.appointment_service.core.dto.UsuarioResponse;
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

    public UsuarioResponse findUsuarioByLogin(String login) {
        String url = orchestratorBaseUrl + "/api/orchestrator/usuario/login/{login}";
        Supplier<ResponseEntity<UsuarioResponse>> call = () -> restTemplate.exchange(url, HttpMethod.GET, null,
                UsuarioResponse.class, login);

        UsuarioResponse usuario = invokeForBody("findUsuarioByLogin", call);
        return sanitizeUsuarioResponse(usuario);
    }

    private UsuarioResponse sanitizeUsuarioResponse(UsuarioResponse original) {
        if (original == null) {
            return null;
        }
        // create a shallow copy without the senha field to avoid leaking password hashes
        UsuarioResponse copy = UsuarioResponse.builder()
                .id(original.getId())
                .login(original.getLogin())
                .senha(null)
                .nome(original.getNome())
                .email(original.getEmail())
                .cpf(original.getCpf())
                .ativo(original.isAtivo())
                .createdAt(original.getCreatedAt())
                .updatedAt(original.getUpdatedAt())
                .perfil(original.getPerfil())
                .build();

        return copy;
    }

    public ResponseEntity<Object> createUsuario(Object payload) {
        String url = orchestratorBaseUrl + "/api/usuario/criacao";
        Supplier<ResponseEntity<Object>> call = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        };

        return invokeForResponse("createUsuario", call);
    }

    public ResponseEntity<Object> createUsuarioWithHeaders(Object payload, HttpHeaders incomingHeaders) {
        String url = orchestratorBaseUrl + "/api/usuario/criacao";
        Supplier<ResponseEntity<Object>> call = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // forward selected headers
            if (incomingHeaders != null) {
                if (incomingHeaders.containsKey("X-User-Profile")) {
                    headers.put("X-User-Profile", incomingHeaders.get("X-User-Profile"));
                }
                if (incomingHeaders.containsKey("X-Username")) {
                    headers.put("X-Username", incomingHeaders.get("X-Username"));
                }
                // also forward Authorization if present
                if (incomingHeaders.containsKey(HttpHeaders.AUTHORIZATION)) {
                    headers.put(HttpHeaders.AUTHORIZATION, incomingHeaders.get(HttpHeaders.AUTHORIZATION));
                }
            }

            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        };

        return invokeForResponse("createUsuario", call);
    }

    public ResponseEntity<Object> loginUsuarioWithHeaders(Object payload, HttpHeaders incomingHeaders) {
        String url = orchestratorBaseUrl + "/api/usuario/login";
        Supplier<ResponseEntity<Object>> call = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (incomingHeaders != null) {
                if (incomingHeaders.containsKey("X-User-Profile")) {
                    headers.put("X-User-Profile", incomingHeaders.get("X-User-Profile"));
                }
                if (incomingHeaders.containsKey("X-Username")) {
                    headers.put("X-Username", incomingHeaders.get("X-Username"));
                }
                if (incomingHeaders.containsKey(HttpHeaders.AUTHORIZATION)) {
                    headers.put(HttpHeaders.AUTHORIZATION, incomingHeaders.get(HttpHeaders.AUTHORIZATION));
                }
            }

            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        };

        return invokeForResponse("loginUsuario", call);
    }

    public ResponseEntity<Object> updateSenhaWithHeaders(Object payload, HttpHeaders incomingHeaders) {
        String url = orchestratorBaseUrl + "/api/usuario/login/atualiza-senha";
        Supplier<ResponseEntity<Object>> call = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (incomingHeaders != null) {
                if (incomingHeaders.containsKey("X-User-Profile")) {
                    headers.put("X-User-Profile", incomingHeaders.get("X-User-Profile"));
                }
                if (incomingHeaders.containsKey("X-Username")) {
                    headers.put("X-Username", incomingHeaders.get("X-Username"));
                }
                if (incomingHeaders.containsKey(HttpHeaders.AUTHORIZATION)) {
                    headers.put(HttpHeaders.AUTHORIZATION, incomingHeaders.get(HttpHeaders.AUTHORIZATION));
                }
            }

            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class);
        };

        return invokeForResponse("updateSenhaUsuario", call);
    }

    public ResponseEntity<Object> createAgendamentoWithHeaders(Object payload, HttpHeaders incomingHeaders) {
        String url = orchestratorBaseUrl + "/api/agendamento/criacao";
        Supplier<ResponseEntity<Object>> call = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (incomingHeaders != null) {
                if (incomingHeaders.containsKey("X-User-Profile")) {
                    headers.put("X-User-Profile", incomingHeaders.get("X-User-Profile"));
                }
                if (incomingHeaders.containsKey("X-Username")) {
                    headers.put("X-Username", incomingHeaders.get("X-Username"));
                }
                if (incomingHeaders.containsKey(HttpHeaders.AUTHORIZATION)) {
                    headers.put(HttpHeaders.AUTHORIZATION, incomingHeaders.get(HttpHeaders.AUTHORIZATION));
                }
            }

            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        };

        return invokeForResponse("createAgendamento", call);
    }

    public ResponseEntity<Object> updateAgendamentoWithHeaders(Long id, Object payload, HttpHeaders incomingHeaders) {
        String url = orchestratorBaseUrl + "/api/agendamento/edicao/{id}";
        Supplier<ResponseEntity<Object>> call = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (incomingHeaders != null) {
                if (incomingHeaders.containsKey("X-User-Profile")) {
                    headers.put("X-User-Profile", incomingHeaders.get("X-User-Profile"));
                }
                if (incomingHeaders.containsKey("X-Username")) {
                    headers.put("X-Username", incomingHeaders.get("X-Username"));
                }
                if (incomingHeaders.containsKey(HttpHeaders.AUTHORIZATION)) {
                    headers.put(HttpHeaders.AUTHORIZATION, incomingHeaders.get(HttpHeaders.AUTHORIZATION));
                }
            }

            HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class, id);
        };

        return invokeForResponse("updateAgendamento", call);
    }

    private <T> T invokeForBody(String name, Supplier<ResponseEntity<T>> restCall) {
        Supplier<T> supplier = () -> {
            try {
                ResponseEntity<T> resp = restCall.get();
                return resp == null ? null : resp.getBody();
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

}