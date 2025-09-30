package com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.core.exceptions.BusinessException;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarEspecialidadeExistente;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarNomeEspecialidadeDuplicado;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.AtualizarEspecialidadeCommandDto;
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
