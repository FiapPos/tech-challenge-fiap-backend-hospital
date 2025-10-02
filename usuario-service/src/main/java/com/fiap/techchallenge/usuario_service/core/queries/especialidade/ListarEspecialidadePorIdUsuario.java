package com.fiap.techchallenge.usuario_service.core.queries.especialidade;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.especialidade.ListarEspecialidadePorResultadoItem;
import com.fiap.techchallenge.usuario_service.core.utils.ValidarPerfilMedico;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListarEspecialidadePorIdUsuario {

    private final ValidarUsuarioExistente validarUsuarioExistente;
    private final ValidarPerfilMedico validarPerfilMedico;

    public List<ListarEspecialidadePorResultadoItem> execute(Long usuarioId) {
        Usuario usuario = validarUsuarioExistente.execute(usuarioId);
        validarPerfilMedico.execute(usuario);
        List<Especialidade> especialidades = usuario.getEspecialidades();
        return mapToResultadoItemList(especialidades);
    }

    private List<ListarEspecialidadePorResultadoItem> mapToResultadoItemList(List<Especialidade> especialidades) {
        if (especialidades == null)
            return List.of();
        return especialidades.stream()
                .map(this::mapToResultadoItem)
                .collect(Collectors.toList());
    }

    private ListarEspecialidadePorResultadoItem mapToResultadoItem(Especialidade especialidade) {
        return ListarEspecialidadePorResultadoItem.builder()
                .id(especialidade.getId())
                .nome(especialidade.getNome())
                .descricao(especialidade.getDescricao())
                .ativo(especialidade.isAtivo())
                .dataCriacao(especialidade.getDataCriacao())
                .dataAtualizacao(especialidade.getDataAtualizacao())
                .build();
    }
}
