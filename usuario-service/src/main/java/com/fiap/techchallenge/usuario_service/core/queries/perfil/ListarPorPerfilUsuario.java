package com.fiap.techchallenge.usuario_service.core.queries.perfil;

import com.fiap.techchallenge.usuario_service.core.domain.entities.PerfilDoUsuario;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ListarPorPerfilUsuario {

    public List<PerfilUsuarioResultItem> execute(Set<PerfilDoUsuario> perfilUsuario) {
        return perfilUsuario.stream()
                .map(perfilDoUsuario -> {
                    Perfil perfil = perfilDoUsuario.getPerfil();
                    return PerfilUsuarioResultItem.builder()
                            .perfil(perfil)
                            .codigo(perfil.getCodigo())
                            .build();
                })
                .toList();
    }
}
