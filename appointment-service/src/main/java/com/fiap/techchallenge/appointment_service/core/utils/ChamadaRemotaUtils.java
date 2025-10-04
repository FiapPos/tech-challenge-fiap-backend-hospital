package com.fiap.techchallenge.appointment_service.core.utils;

import com.fiap.techchallenge.appointment_service.core.client.OrchestratorException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;

import java.util.function.Supplier;

/**
 * Utilitário para encapsular chamadas remotas com retry + circuit-breaker e tratamento
 * de RestClientResponseException.
 */
public final class ChamadaRemotaUtils {

    private ChamadaRemotaUtils() {
    }

    public static ResponseEntityWrapper invocarParaResposta(String nome,
                                                           Supplier<ResponseEntity<Object>> chamadaRest,
                                                           CircuitBreakerFactory<?, ?> cbf,
                                                           RetryRegistry retryRegistry) {

        Supplier<ResponseEntity<Object>> supplierTratado = () -> {
            try {
                return chamadaRest.get();
            } catch (RestClientResponseException ex) {
                HttpStatusCode statusCode = ex.getStatusCode();
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

        Supplier<ResponseEntity<Object>> executado = executarComResiliencia(nome, supplierTratado, cbf, retryRegistry);
        return new ResponseEntityWrapper(executado);
    }

    private static <T> Supplier<T> executarComResiliencia(String nome,
                                                          Supplier<T> supplier,
                                                          CircuitBreakerFactory<?, ?> cbf,
                                                          RetryRegistry retryRegistry) {
        Supplier<T> effective = supplier;

        if (retryRegistry != null) {
            Retry retry = retryRegistry.retry(nome);
            effective = () -> Retry.decorateSupplier(retry, supplier).get();
        }

        if (cbf == null) {
            return effective;
        }

        return () -> cbf.create(nome).run(effective::get, throwable -> {
            if (throwable instanceof OrchestratorException) {
                throw (OrchestratorException) throwable;
            }
            throw new RuntimeException(throwable);
        });
    }

    public static class ResponseEntityWrapper {
        private final Supplier<ResponseEntity<Object>> supplier;

        public ResponseEntityWrapper(Supplier<ResponseEntity<Object>> supplier) {
            this.supplier = supplier;
        }

        public ResponseEntity<Object> asResponseEntity() {
            return supplier.get();
        }
    }
}
