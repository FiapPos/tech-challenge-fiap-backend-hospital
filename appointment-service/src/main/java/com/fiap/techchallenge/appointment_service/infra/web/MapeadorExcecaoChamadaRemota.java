package com.fiap.techchallenge.appointment_service.infra.web;

import com.fiap.techchallenge.appointment_service.infra.client.OrchestratorException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientResponseException;

import java.util.function.Supplier;

public final class MapeadorExcecaoChamadaRemota {

    private MapeadorExcecaoChamadaRemota() {
    }

    public static <T> Supplier<T> envolver(Supplier<T> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (RestClientResponseException ex) {
                throw mapRestClientException(ex);
            } catch (FeignException fe) {
                throw mapFeignException(fe);
            }
        };
    }

    private static OrchestratorException mapRestClientException(RestClientResponseException ex) {
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
        return new OrchestratorException(status, body == null ? "" : body);
    }

    private static OrchestratorException mapFeignException(FeignException fe) {
        int status = 500;
        try {
            status = fe.status();
        } catch (Exception ignored) {
        }
        String body = null;
        try {
            body = fe.contentUTF8();
        } catch (Exception ignored) {
        }
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            httpStatus = HttpStatus.valueOf(status);
        } catch (Exception ignored) {
        }
        return new OrchestratorException(httpStatus, body == null ? "" : body);
    }
}
