package br.com.fiap.techchallenge.infrastructure.api.controllers;

import br.com.fiap.techchallenge.core.domain.usecases.usuario.AtualizarUsuarioComando;
import br.com.fiap.techchallenge.core.domain.usecases.usuario.CriarUsuarioComando;
import br.com.fiap.techchallenge.core.domain.usecases.usuario.DesativarUsuarioComando;
import br.com.fiap.techchallenge.core.dtos.usuario.AtualizarUsuarioComandoDto;
import br.com.fiap.techchallenge.core.dtos.usuario.CriarUsuarioComandoDto;
import br.com.fiap.techchallenge.core.queries.usuario.ListarUsuariosQuery;
import br.com.fiap.techchallenge.core.queries.usuario.ListarUsuariosPorIdEspecialidadeQuery;
import br.com.fiap.techchallenge.core.queries.params.ListarUsuariosParams;
import br.com.fiap.techchallenge.core.queries.resultadoItem.usuario.ListarUsuariosResultadoItem;
import br.com.fiap.techchallenge.core.utils.doc.UsuarioControllerDoc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements UsuarioControllerDoc {

        private final CriarUsuarioComando criarUsuarioComando;
        private final ListarUsuariosQuery listarUsuariosQuery;
        private final AtualizarUsuarioComando atualizarUsuarioComando;
        private final DesativarUsuarioComando desativarUsuarioComando;
        private final ListarUsuariosPorIdEspecialidadeQuery listarUsuariosPorIdEspecialidadeQuery;

        @PostMapping
        public ResponseEntity<Void> criarUsuario(@RequestBody @Valid CriarUsuarioComandoDto criarUsuarioComandoDto) {
                criarUsuarioComando.execute(criarUsuarioComandoDto);
                return new ResponseEntity<>(HttpStatus.CREATED);
        }

        @GetMapping
        public ResponseEntity<List<ListarUsuariosResultadoItem>> listarUsuarios(ListarUsuariosParams params) {
                List<ListarUsuariosResultadoItem> resultado = listarUsuariosQuery.execute(params);
                if (resultado.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(resultado);
        }

        @GetMapping("/por-especialidade/{especialidadeId}")
        public ResponseEntity<List<ListarUsuariosResultadoItem>> listarUsuariosPorEspecialidade(
                        @PathVariable Long especialidadeId) {
                List<ListarUsuariosResultadoItem> resultado = listarUsuariosPorIdEspecialidadeQuery
                                .execute(especialidadeId);
                if (resultado.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(resultado);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Void> atualizarUsuario(@PathVariable Long id,
                        @RequestBody AtualizarUsuarioComandoDto dto) {
                atualizarUsuarioComando.execute(id, dto);
                return ResponseEntity.ok().build();
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
                desativarUsuarioComando.execute(id);
                return ResponseEntity.ok().build();
        }
}