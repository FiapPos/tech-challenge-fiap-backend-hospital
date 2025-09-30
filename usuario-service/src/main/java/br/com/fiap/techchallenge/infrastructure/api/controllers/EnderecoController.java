package br.com.fiap.techchallenge.infrastructure.api.controllers;

import br.com.fiap.techchallenge.core.domain.entities.Endereco;
import br.com.fiap.techchallenge.core.domain.usecases.endereco.AtualizarEnderecoComando;
import br.com.fiap.techchallenge.core.domain.usecases.endereco.CriarEnderecoCommand;
import br.com.fiap.techchallenge.core.domain.usecases.endereco.DeletarEnderecoComando;
import br.com.fiap.techchallenge.core.dtos.endereco.AtualizarEnderecoComandoDto;
import br.com.fiap.techchallenge.core.dtos.endereco.CriarEnderecoComandoDto;
import br.com.fiap.techchallenge.core.dtos.endereco.DeletarEnderecoComandoDto;
import br.com.fiap.techchallenge.core.exceptions.BadRequestException;
import br.com.fiap.techchallenge.core.gateways.EnderecoRepository;
import br.com.fiap.techchallenge.core.queries.endereco.ListarEnderecoPorIdUsuario;
import br.com.fiap.techchallenge.core.queries.resultadoItem.endereco.ListarEnderecoPorIdUsuarioResultadoItem;
import br.com.fiap.techchallenge.core.utils.doc.EnderecoControllerDoc;
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