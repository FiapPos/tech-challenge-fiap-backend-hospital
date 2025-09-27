package br.com.fiap.techchallenge.core.domain.usecases.especialidade;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import br.com.fiap.techchallenge.core.exceptions.BusinessException;
import br.com.fiap.techchallenge.infrastructure.services.ValidarEspecialidadeExistente;
import br.com.fiap.techchallenge.infrastructure.services.ValidarNomeEspecialidadeDuplicado;
import br.com.fiap.techchallenge.core.dtos.especialidade.AtualizarEspecialidadeCommandDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AtualizarEspecialidadeComando {

    private final EspecialidadeRepository repository;
    private final ValidarEspecialidadeExistente validarEspecialidadeExistente;
    private final ValidarNomeEspecialidadeDuplicado validarNomeEspecialidadeDuplicado;

    public Especialidade execute(Long id, AtualizarEspecialidadeCommandDto dto) {
        validarDto(dto);
        Especialidade existente = validarEspecialidadeExistente.execute(id);
        atualizarCampos(existente, dto);

        return repository.save(existente);
    }

    private void validarDto(AtualizarEspecialidadeCommandDto dto) {
        if ((dto.novoNome() == null || dto.novoNome().isBlank()) && dto.novaDescricao() == null) {
            throw new BusinessException("atualizar.especialidade.nenhum.campo");
        }
    }

    private void atualizarCampos(Especialidade existente, AtualizarEspecialidadeCommandDto dto) {

        if (dto.novoNome() != null && !dto.novoNome().isBlank()) {
            validarNomeEspecialidadeDuplicado.execute(dto.novoNome().trim());
            existente.setNome(dto.novoNome().trim());
        }
        if (dto.novaDescricao() != null) {
            existente.setDescricao(dto.novaDescricao());
        }
        existente.setDataAtualizacao(LocalDateTime.now());
    }
}
