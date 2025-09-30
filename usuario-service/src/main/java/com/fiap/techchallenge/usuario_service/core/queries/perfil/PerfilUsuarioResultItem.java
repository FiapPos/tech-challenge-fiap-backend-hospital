package com.fiap.techchallenge.usuario_service.core.queries.perfil;

import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
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
