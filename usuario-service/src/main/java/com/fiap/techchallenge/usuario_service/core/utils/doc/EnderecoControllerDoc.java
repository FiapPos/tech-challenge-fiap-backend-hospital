package com.fiap.techchallenge.usuario_service.core.utils.doc;

import com.fiap.techchallenge.usuario_service.core.dtos.endereco.AtualizarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.dtos.endereco.CriarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.dtos.endereco.DeletarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.queries.endereco.ListarEnderecoPorIdUsuario;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Endereços", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um endereço")
public interface EnderecoControllerDoc {

        @Operation(summary = "Criar um novo endereço.", description = "Recurso para criar um novo endereço.", responses = {
                        @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriarEnderecoComandoDto.class))),
                        @ApiResponse(responseCode = "409", description = "Endereço já existente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
                        @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })
        ResponseEntity<Void> criar(@RequestBody @Valid CriarEnderecoComandoDto dto);

        @Operation(summary = "Atualizar endereço do Usuário.", description = "Recurso para atualizar o endereço de um usuário.", responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AtualizarEnderecoComandoDto.class))),
                        @ApiResponse(responseCode = "422", description = "Recurso não atualizado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })
        ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarEnderecoComandoDto dto);

        @Operation(summary = "Deletar um endereço.", description = "Recurso para deletar um endereço.", responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso deletado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeletarEnderecoComandoDto.class))),
                        @ApiResponse(responseCode = "422", description = "Recurso não pode ser deletado por dados de entradas inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })
        ResponseEntity<Void> deletar(@RequestBody DeletarEnderecoComandoDto dto);

        @Operation(summary = "Listar endereço por ID.", description = "Recurso para listar endereço do usuário por ID.", responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso listado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListarEnderecoPorIdUsuario.class))),
                        @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })
        ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> listarPorUsuario(@PathVariable Long id);
}
