package com.fiap.techchallenge.usuario_service.core.utils.doc;

import com.fiap.techchallenge.usuario_service.core.dtos.login.AtualizaCredenciaisComandoDto;
import com.fiap.techchallenge.usuario_service.core.dtos.login.CredenciaisUsuarioDto;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "Login", description = "Contém as operações de autenticação e atualização de credenciais do usuário.")
public interface LoginControllerDoc {

    @Operation(summary = "Autenticar login do usuário.", description = "Gera um token JWT para o usuário autenticado com login e senha válidos.", responses = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou campos obrigatórios não preenchidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    ResponseEntity<Map<String, String>> login(@RequestBody @Valid CredenciaisUsuarioDto credentials,
            BindingResult bindingResult) throws Exception;

    @Operation(summary = "Atualizar senha do usuário.", description = "Recurso para atualizar a senha de um usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AtualizaCredenciaisComandoDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não atualizado por dados de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestException.class)))
    })
    ResponseEntity<Map<String, String>> atualizaSenha(
            @RequestBody @Valid AtualizaCredenciaisComandoDto atualizaCredenciaisComandoDto,
            BindingResult bindingResult);
}
