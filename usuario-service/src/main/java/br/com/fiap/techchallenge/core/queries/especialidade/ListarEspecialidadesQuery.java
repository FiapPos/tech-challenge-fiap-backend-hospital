package br.com.fiap.techchallenge.core.queries.especialidade;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import br.com.fiap.techchallenge.core.queries.resultadoItem.especialidade.ListarEspecialidadePorResultadoItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarEspecialidadesQuery {

    private final EspecialidadeRepository especialidadeRepository;

    // Única responsabilidade: listar todas as especialidades disponíveis
    public List<ListarEspecialidadePorResultadoItem> execute() {
        return especialidadeRepository.findAll().stream()
                .map(this::mapear)
                .toList();
    }

    private ListarEspecialidadePorResultadoItem mapear(Especialidade e) {
        return ListarEspecialidadePorResultadoItem.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descricao(e.getDescricao())
                .ativo(e.isAtivo())
                .dataCriacao(e.getDataCriacao())
                .dataAtualizacao(e.getDataAtualizacao())
                .build();
    }
}
