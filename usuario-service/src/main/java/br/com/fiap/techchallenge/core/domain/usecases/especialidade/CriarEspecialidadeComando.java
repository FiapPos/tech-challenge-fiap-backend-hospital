package br.com.fiap.techchallenge.core.domain.usecases.especialidade;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.gateways.EspecialidadeRepository;
import br.com.fiap.techchallenge.core.exceptions.BusinessException;
import br.com.fiap.techchallenge.core.dtos.especialidade.CriarEspecialidadeCommandDto;
import br.com.fiap.techchallenge.infrastructure.services.ValidarNomeEspecialidadeDuplicado;
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
