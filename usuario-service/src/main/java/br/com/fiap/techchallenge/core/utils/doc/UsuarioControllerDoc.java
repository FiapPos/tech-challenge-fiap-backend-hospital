package br.com.fiap.techchallenge.core.utils.doc;

import br.com.fiap.techchallenge.core.domain.usecases.usuario.DesativarUsuarioComando;
import br.com.fiap.techchallenge.core.dtos.usuario.AtualizarUsuarioComandoDto;
import br.com.fiap.techchallenge.core.dtos.usuario.CriarUsuarioComandoDto;
import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import br.com.fiap.techchallenge.core.queries.params.ListarUsuariosParams;
import br.com.fiap.techchallenge.core.queries.resultadoItem.usuario.ListarUsuariosResultadoItem;
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

@Tag(name = "Usuários", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário.")
public interface UsuarioControllerDoc {

        @Operation(summary = "Criar um novo usuário.", description = "Recurso para criar um novo usuário.", responses = {
                        @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CriarUsuarioComandoDto.class))),
                        @ApiResponse(responseCode = "409", description = "Usuário já existente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
                        @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })
        ResponseEntity<Void> criarUsuario(@RequestBody @Valid CriarUsuarioComandoDto criarUsuarioComandoDto);

        @Operation(summary = "Listar usuários.", description = "Recurso para listar usuários com filtros opcionais.", responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso listado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListarUsuariosResultadoItem.class))),
                        @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado."),
                        @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })
        ResponseEntity<List<ListarUsuariosResultadoItem>> listarUsuarios(ListarUsuariosParams params);

        @Operation(summary = "Listar usuários por especialidade.", description = "Retorna os usuários associados a uma especialidade.", responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso listado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListarUsuariosResultadoItem.class))),
                        @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado."),
                        @ApiResponse(responseCode = "404", description = "Especialidade não encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })
        ResponseEntity<List<ListarUsuariosResultadoItem>> listarUsuariosPorEspecialidade(
                        @PathVariable Long especialidadeId);

        @Operation(summary = "Atualizar o usuário.", description = "Recurso para atualizar os dados de um usuário.", responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AtualizarUsuarioComandoDto.class))),
                        @ApiResponse(responseCode = "403", description = "Acesso negado - usuário não tem permissão para alterar este usuário.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
                        @ApiResponse(responseCode = "422", description = "Recurso não atualizado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })
        ResponseEntity<Void> atualizarUsuario(@PathVariable Long id, @RequestBody AtualizarUsuarioComandoDto dto);

        @Operation(summary = "Desativar um usuário.", description = "Recurso para desativar um usuário do sistema.", responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso desativado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DesativarUsuarioComando.class))),
                        @ApiResponse(responseCode = "403", description = "Acesso negado - usuário não tem permissão para desativar usuários.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
                        @ApiResponse(responseCode = "422", description = "Recurso não pode ser desativado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
        })
        ResponseEntity<Void> desativarUsuario(@PathVariable Long id);
}
