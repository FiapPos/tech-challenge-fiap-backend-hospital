package com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.gateways.EspecialidadeRepository;
import com.fiap.techchallenge.usuario_service.core.exceptions.BusinessException;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.CriarEspecialidadeCommandDto;
import com.fiap.techchallenge.usuario_service.infrastructure.services.ValidarNomeEspecialidadeDuplicado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CriarEspecialidadeComando {

    private final EspecialidadeRepository especialidadeRepository;
    private final ValidarNomeEspecialidadeDuplicado validarNomeEspecialidadeDuplicado;

    /**
     * Cria uma especialidade caso não exista outra com o mesmo nome.
     * Regras:
     * - nome obrigatório (não nulo / não em branco)
     * - nome único (case insensitive)
     */
    public Especialidade execute(CriarEspecialidadeCommandDto dto) {
        validarEntrada(dto.novoNome());
        validarNomeEspecialidadeDuplicado.execute(dto.novoNome().trim());

        Especialidade especialidade = Especialidade.builder()
                .nome(dto.novoNome().trim())
                .descricao(dto.novaDescricao())
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .build();

        return especialidadeRepository.save(especialidade);
    }

    private void validarEntrada(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("especialidade.nome.obrigatorio");
        }
    }
}
