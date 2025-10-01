package com.fiap.techchallenge.appointment_service.core.service;

import com.fiap.techchallenge.appointment_service.core.document.AuthEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventosAtenticacao {

    public void processarEventoAutenticacao(AuthEvent authEvent) {
        log.info("Processando evento de autenticação: {} para usuário: {}",
                authEvent.getEventType(), authEvent.getUsername());
        switch (authEvent.getStatus()) {
            case SUCCESS -> log.info("Evento de autenticação bem-sucedido processado para usuário: {}",
                    authEvent.getUsername());
            case FAIL -> log.warn("Evento de falha de autenticação processado para usuário: {}",
                    authEvent.getUsername());
            default -> log.debug("Evento de autenticação processado: {}", authEvent.getStatus());
        }
    }

    public void processarEventoAutorizacao(AuthEvent authEvent) {
        log.info("Processando evento de autorização: {} para usuário: {}",
                authEvent.getEventType(), authEvent.getUsername());
        switch (authEvent.getStatus()) {
            case SUCCESS -> log.info("Evento de autorização bem-sucedido processado para usuário: {} com perfil: {}",
                    authEvent.getUsername(), authEvent.getUserProfile());
            case FAIL -> log.warn("Evento de falha de autorização processado para usuário: {} com perfil: {}",
                    authEvent.getUsername(), authEvent.getUserProfile());
            default -> log.debug("Evento de autorização processado: {}", authEvent.getStatus());
        }
    }

    public void processarEventoGeralAuth(AuthEvent authEvent) {
        log.info("Processando evento geral de auth: {} para usuário: {}",
                authEvent.getEventType(), authEvent.getUsername());
        if (authEvent.getEventType() != null) {
            switch (authEvent.getEventType().toLowerCase()) {
                case "login_success" -> registrarLoginSucesso(authEvent);
                case "login_failed" -> registrarFalhaLogin(authEvent);
                case "token_validated" -> registrarTokenValidado(authEvent);
                case "token_expired" -> registrarTokenExpirado(authEvent);
                case "logout" -> registrarLogout(authEvent);
                default -> log.debug("Evento de auth desconhecido: {}", authEvent.getEventType());
            }
        }
    }

    private void registrarLoginSucesso(AuthEvent authEvent) {
        log.info("Login bem-sucedido registrado para usuário: {}", authEvent.getUsername());
    }

    private void registrarFalhaLogin(AuthEvent authEvent) {
        log.warn("Falha de login registrada para usuário: {}", authEvent.getUsername());
    }

    private void registrarTokenValidado(AuthEvent authEvent) {
        log.debug("Token validado para usuário: {}", authEvent.getUsername());
    }

    private void registrarTokenExpirado(AuthEvent authEvent) {
        log.info("Token expirado para usuário: {}", authEvent.getUsername());
    }

    private void registrarLogout(AuthEvent authEvent) {
        log.info("Logout registrado para usuário: {}", authEvent.getUsername());
    }
}
