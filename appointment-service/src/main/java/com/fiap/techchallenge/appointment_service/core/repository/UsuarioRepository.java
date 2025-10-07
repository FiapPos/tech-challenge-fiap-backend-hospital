package com.fiap.techchallenge.appointment_service.core.repository;

public interface UsuarioRepository {
    boolean existsByLogin(String login);
}
