package com.fiap.techchallenge.usuario_service.infrastructure.api.controllers;

import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidadesDoMedico.AssociarEspecialidadeAoMedico;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidadesDoMedico.DesassociarEspecialidadeMedico;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidadesDoMedico.AssociarEspecialidadeMedicoDto;
import com.fiap.techchallenge.usuario_service.core.queries.especialidade.ListarEspecialidadePorIdUsuario;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.especialidade.ListarEspecialidadePorResultadoItem;
import lombok.RequiredArgsConstructor;
import com.fiap.techchallenge.usuario_service.core.utils.doc.EspecialidadesMedicoControllerDoc;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos/{medicoId}/especialidades")
@RequiredArgsConstructor

public class EspecialidadesMedicoController implements EspecialidadesMedicoControllerDoc {
    private final AssociarEspecialidadeAoMedico associarEspecialidadeAoMedico;
    private final DesassociarEspecialidadeMedico desassociarEspecialidadeMedico;
    private final ListarEspecialidadePorIdUsuario listarEspecialidadePorIdUsuario;

    @PostMapping("/{especialidadeId}")
    public ResponseEntity<Void> associarEspecialidade(@PathVariable Long medicoId, @PathVariable Long especialidadeId) {
        AssociarEspecialidadeMedicoDto dto = new AssociarEspecialidadeMedicoDto(especialidadeId);
        associarEspecialidadeAoMedico.execute(medicoId, dto);
        URI location = URI.create(String.format("/medicos/%d/especialidades/%d", medicoId, especialidadeId));
        return ResponseEntity.created(location).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ListarEspecialidadePorResultadoItem>> listarEspecialidades(@PathVariable Long medicoId) {
        List<ListarEspecialidadePorResultadoItem> result = listarEspecialidadePorIdUsuario.execute(medicoId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{especialidadeId}")
    public ResponseEntity<Void> desassociarEspecialidade(@PathVariable Long medicoId,
            @PathVariable Long especialidadeId) {
        desassociarEspecialidadeMedico.execute(medicoId, especialidadeId);
        return ResponseEntity.noContent().build();
    }
}
