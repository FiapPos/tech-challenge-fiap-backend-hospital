package com.fiap.techchallenge.usuario_service.infrastructure.api.controllers;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Especialidade;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade.AtualizarEspecialidadeComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade.CriarEspecialidadeComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade.InativarEspecialidadeComando;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.CriarEspecialidadeCommandDto;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.AtualizarEspecialidadeCommandDto;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.EspecialidadeResponse;
import com.fiap.techchallenge.usuario_service.core.queries.especialidade.BuscaEspecialidadePorIdQuery;
import com.fiap.techchallenge.usuario_service.core.queries.especialidade.ListarEspecialidadesQuery;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.especialidade.EncontraEspecialidadeItem;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.especialidade.ListarEspecialidadePorResultadoItem;
import com.fiap.techchallenge.usuario_service.core.utils.doc.EspecialidadeControllerDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadeController implements EspecialidadeControllerDoc {

    private final CriarEspecialidadeComando criarEspecialidadeComando;
    private final ListarEspecialidadesQuery listarEspecialidadesQuery;
    private final AtualizarEspecialidadeComando atualizarEspecialidadeComando;
    private final InativarEspecialidadeComando inativarEspecialidadeComando;
    private final BuscaEspecialidadePorIdQuery buscaEspecialidadePorIdQuery;

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

    @GetMapping("/{id}")
    public ResponseEntity<EncontraEspecialidadeItem> buscaEspecialidade(@PathVariable Long id) {
        return ResponseEntity.ok(buscaEspecialidadePorIdQuery.execute(id));
    }
}
