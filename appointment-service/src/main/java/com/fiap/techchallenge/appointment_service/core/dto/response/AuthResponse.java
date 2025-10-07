package com.fiap.techchallenge.appointment_service.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String type;
    private Long userId;
    private String login;
    private String nome;
    private String email;
    private String perfil;
    private LocalDateTime expiresAt;
}