package br.com.fiap.techchallenge.core.queries.perfil;

import br.com.fiap.techchallenge.core.domain.entities.PerfilDoUsuario;
import br.com.fiap.techchallenge.core.enums.Perfil;
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
