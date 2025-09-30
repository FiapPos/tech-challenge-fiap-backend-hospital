package com.fiap.techchallenge.usuario_service.core.utils.doc;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.especialidade.ListarEspecialidadePorResultadoItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Especialidades do Médico", description = "Operações para associar, listar e desassociar especialidades de médicos.")
public interface EspecialidadesMedicoControllerDoc {

    @Operation(summary = "Associar especialidade ao médico.", description = "Associa uma especialidade a um médico.", responses = {
            @ApiResponse(responseCode = "201", description = "Especialidade associada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Médico ou especialidade não encontrados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    ResponseEntity<Void> associarEspecialidade(@PathVariable Long medicoId, @PathVariable Long especialidadeId);

    @Operation(summary = "Listar especialidades do médico.", description = "Retorna as especialidades associadas a um médico.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListarEspecialidadePorResultadoItem.class))),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    ResponseEntity<List<ListarEspecialidadePorResultadoItem>> listarEspecialidades(@PathVariable Long medicoId);

    @Operation(summary = "Desassociar especialidade do médico.", description = "Remove a associação de uma especialidade de um médico.", responses = {
            @ApiResponse(responseCode = "204", description = "Especialidade desassociada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Médico ou especialidade não encontrados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    ResponseEntity<Void> desassociarEspecialidade(@PathVariable Long medicoId, @PathVariable Long especialidadeId);
}
