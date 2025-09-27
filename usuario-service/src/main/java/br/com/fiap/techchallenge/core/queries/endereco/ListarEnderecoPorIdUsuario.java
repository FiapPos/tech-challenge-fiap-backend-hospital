package br.com.fiap.techchallenge.core.queries.endereco;

import br.com.fiap.techchallenge.core.domain.entities.Endereco;
import br.com.fiap.techchallenge.core.gateways.EnderecoRepository;
import br.com.fiap.techchallenge.core.queries.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import br.com.fiap.techchallenge.infrastructure.services.ValidarUsuarioExistente;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListarEnderecoPorIdUsuario {

    private final EnderecoRepository enderecoRepository;
    private final ValidarUsuarioExistente validarUsuarioExistente;

    public List<ListarEnderecoPorIdUsuarioResultadoItem> execute(Long usuarioId) {
        validarUsuarioExistente.execute(usuarioId);
        List<Endereco> enderecos = buscarEnderecos(usuarioId);
        return mapToResultadoItemList(enderecos);
    }

    private List<Endereco> buscarEnderecos(Long usuarioId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return enderecoRepository.findByUsuarioId(usuarioId, sort);
    }

    private List<ListarEnderecoPorIdUsuarioResultadoItem> mapToResultadoItemList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(this::mapToResultadoItem)
                .collect(Collectors.toList());
    }

    private ListarEnderecoPorIdUsuarioResultadoItem mapToResultadoItem(Endereco endereco) {
        return ListarEnderecoPorIdUsuarioResultadoItem.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .cep(endereco.getCep())
                .numero(endereco.getNumero())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .usuarioId(endereco.getUsuarioId())
                .dataCriacao(endereco.getDataCriacao())
                .dataAtualizacao(endereco.getDataAtualizacao())
                .build();
    }
}