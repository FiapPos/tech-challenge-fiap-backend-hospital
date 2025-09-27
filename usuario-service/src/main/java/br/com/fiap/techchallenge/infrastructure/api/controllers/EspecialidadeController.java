package br.com.fiap.techchallenge.infrastructure.api.controllers;

import br.com.fiap.techchallenge.core.domain.entities.Especialidade;
import br.com.fiap.techchallenge.core.domain.usecases.especialidade.*;
import br.com.fiap.techchallenge.core.dtos.especialidade.CriarEspecialidadeCommandDto;
import br.com.fiap.techchallenge.core.dtos.especialidade.AtualizarEspecialidadeCommandDto;
import br.com.fiap.techchallenge.core.dtos.especialidade.EspecialidadeResponse;
import br.com.fiap.techchallenge.core.queries.especialidade.ListarEspecialidadesQuery;
import br.com.fiap.techchallenge.core.queries.resultadoItem.especialidade.ListarEspecialidadePorResultadoItem;
import br.com.fiap.techchallenge.core.utils.doc.EspecialidadeControllerDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/especialidades")
@RequiredArgsConstructor
public class EspecialidadeController implements EspecialidadeControllerDoc {

    private final CriarEspecialidadeComando criarEspecialidadeComando;
    private final ListarEspecialidadesQuery listarEspecialidadesQuery;
    private final AtualizarEspecialidadeComando atualizarEspecialidadeComando;
    private final InativarEspecialidadeComando inativarEspecialidadeComando;

    @PostMapping
    public ResponseEntity<EspecialidadeResponse> criar(@Validated @RequestBody CriarEspecialidadeCommandDto dto) {
        Especialidade criada = criarEspecialidadeComando.execute(dto);
        return ResponseEntity.created(URI.create("/especialidades/" + criada.getId()))
                .body(EspecialidadeResponse.fromDomain(criada));
    }

    @GetMapping
    public ResponseEntity<List<ListarEspecialidadePorResultadoItem>> listar() {
        return ResponseEntity.ok(listarEspecialidadesQuery.execute());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EspecialidadeResponse> atualizar(@PathVariable Long id,
            @Validated @RequestBody AtualizarEspecialidadeCommandDto dto) {
        Especialidade atualizada = atualizarEspecialidadeComando.execute(id, dto);
        return ResponseEntity.ok(EspecialidadeResponse.fromDomain(atualizada));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<EspecialidadeResponse> inativar(@PathVariable Long id) {
        Especialidade inativada = inativarEspecialidadeComando.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(EspecialidadeResponse.fromDomain(inativada));
    }
}
