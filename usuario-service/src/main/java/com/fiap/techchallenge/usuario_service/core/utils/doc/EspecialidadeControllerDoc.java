package com.fiap.techchallenge.usuario_service.core.utils.doc;

import com.fiap.techchallenge.usuario_service.core.domain.usecases.especialidade.InativarEspecialidadeComando;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.AtualizarEspecialidadeCommandDto;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.CriarEspecialidadeCommandDto;
import com.fiap.techchallenge.usuario_service.core.dtos.especialidade.EspecialidadeResponse;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.especialidade.ListarEspecialidadePorResultadoItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Especialidades", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de uma especialidade.")
public interface EspecialidadeControllerDoc {

    @Operation(summary = "Criar uma nova especialidade.", description = "Recurso para criar uma nova especialidade.", responses = {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriarEspecialidadeCommandDto.class))),
            @ApiResponse(responseCode = "409", description = "Especialidade já existente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    ResponseEntity<EspecialidadeResponse> criar(@RequestBody CriarEspecialidadeCommandDto dto);

    @Operation(summary = "Listar especialidades.", description = "Recurso para listar todas as especialidades.", responses = {
            @ApiResponse(responseCode = "200", description = "Recurso listado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListarEspecialidadePorResultadoItem.class))),
            @ApiResponse(responseCode = "204", description = "Nenhuma especialidade encontrada."),
            @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    ResponseEntity<List<ListarEspecialidadePorResultadoItem>> listar();

    @Operation(summary = "Atualizar especialidade.", description = "Recurso para atualizar os dados de uma especialidade.", responses = {
            @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AtualizarEspecialidadeCommandDto.class))),
            @ApiResponse(responseCode = "404", description = "Especialidade não encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não atualizado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    ResponseEntity<EspecialidadeResponse> atualizar(@PathVariable Long id,
            @RequestBody AtualizarEspecialidadeCommandDto dto);

    @Operation(summary = "Inativar especialidade.", description = "Recurso para inativar uma especialidade.", responses = {
            @ApiResponse(responseCode = "200", description = "Recurso inativado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InativarEspecialidadeComando.class))),
            @ApiResponse(responseCode = "404", description = "Especialidade não encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não pode ser inativado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    ResponseEntity<EspecialidadeResponse> inativar(@PathVariable Long id);
}
