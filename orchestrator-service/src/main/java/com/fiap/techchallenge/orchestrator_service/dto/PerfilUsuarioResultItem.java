package com.fiap.techchallenge.orchestrator_service.dto;

import com.fiap.techchallenge.orchestrator_service.enums.Perfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUsuarioResultItem {
    private Perfil perfil;
    private long codigo;
}
