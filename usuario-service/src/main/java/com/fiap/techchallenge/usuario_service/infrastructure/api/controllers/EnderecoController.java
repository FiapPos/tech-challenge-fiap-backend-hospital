package com.fiap.techchallenge.usuario_service.infrastructure.api.controllers;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Endereco;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco.AtualizarEnderecoComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco.CriarEnderecoCommand;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.endereco.DeletarEnderecoComando;
import com.fiap.techchallenge.usuario_service.core.dtos.endereco.AtualizarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.dtos.endereco.CriarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.dtos.endereco.DeletarEnderecoComandoDto;
import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.gateways.EnderecoRepository;
import com.fiap.techchallenge.usuario_service.core.queries.endereco.ListarEnderecoPorIdUsuario;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import com.fiap.techchallenge.usuario_service.core.utils.doc.EnderecoControllerDoc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController implements EnderecoControllerDoc {

    private final CriarEnderecoCommand criarEnderecoCommand;
    private final AtualizarEnderecoComando atualizarEnderecoComando;
    private final DeletarEnderecoComando deletarEnderecoComando;
    private final ListarEnderecoPorIdUsuario listarEnderecoPorIdUsuario;
    private final EnderecoRepository enderecoRepository;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid CriarEnderecoComandoDto dto) {
        criarEnderecoCommand.execute(dto.getUsuarioId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarEnderecoComandoDto dto) {
        Long usuarioId = dto.getUsuarioId();
        atualizarEnderecoComando.execute(id, dto, usuarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestBody DeletarEnderecoComandoDto dto) {
        Endereco endereco = enderecoRepository.findById(dto.getEnderecoId())
                .orElseThrow(() -> new BadRequestException("endereco.nao.encontrado"));
        deletarEnderecoComando.execute(dto.getUsuarioId(), dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<ListarEnderecoPorIdUsuarioResultadoItem>> listarPorUsuario(@PathVariable Long id) {
        List<ListarEnderecoPorIdUsuarioResultadoItem> listarEnderecoPorIdUsuarioResultadoItem = listarEnderecoPorIdUsuario
                .execute(id);
        if (listarEnderecoPorIdUsuarioResultadoItem.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(listarEnderecoPorIdUsuarioResultadoItem);
    }

}