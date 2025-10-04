package com.fiap.techchallenge.usuario_service.core.queries.especialidade;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.exceptions.NotFoundException;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.especialidade.EncontraEspecialidadeItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscaEspecialidadePorIdQuery {

    private final EspecialidadeRepository especialidadeRepository;

    public EncontraEspecialidadeItem execute(Long id) {
        Especialidade especialidade = especialidadeRepository.findById(id).orElseThrow(() -> new NotFoundException("Especialidade não encontrada"));
        return EncontraEspecialidadeItem.builder()
                .id(especialidade.getId())
                .nome(especialidade.getNome())
                .build();

    }
}
