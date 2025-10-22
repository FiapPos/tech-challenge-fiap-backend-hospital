package com.fiap.techchallenge.usuario_service.infrastructure.api.controllers;

import com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario.AtualizarUsuarioComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario.CriarUsuarioComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario.DesativarUsuarioComando;
import com.fiap.techchallenge.usuario_service.core.dtos.usuario.AtualizarUsuarioComandoDto;
import com.fiap.techchallenge.usuario_service.core.dtos.usuario.CriarUsuarioComandoDto;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.usuario.EncontraUsuarioItem;
import com.fiap.techchallenge.usuario_service.core.queries.usuario.BuscaUsuarioPorIdQuery;
import com.fiap.techchallenge.usuario_service.core.queries.usuario.ListarUsuariosQuery;
import com.fiap.techchallenge.usuario_service.core.queries.usuario.ListarUsuariosPorIdEspecialidadeQuery;
import com.fiap.techchallenge.usuario_service.core.queries.usuario.ListarUsuarioPorLoginQuery;
import com.fiap.techchallenge.usuario_service.core.queries.params.ListarUsuariosParams;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.usuario.ListarUsuariosResultadoItem;
import com.fiap.techchallenge.usuario_service.core.utils.doc.UsuarioControllerDoc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements UsuarioControllerDoc {

        private final CriarUsuarioComando criarUsuarioComando;
        private final ListarUsuariosQuery listarUsuariosQuery;
        private final AtualizarUsuarioComando atualizarUsuarioComando;
        private final DesativarUsuarioComando desativarUsuarioComando;
        private final ListarUsuariosPorIdEspecialidadeQuery listarUsuariosPorIdEspecialidadeQuery;
        private final ListarUsuarioPorLoginQuery listarUsuarioPorLoginQuery;
        private final BuscaUsuarioPorIdQuery buscaUsuarioPorIdQuery;

        @PostMapping
        @PreAuthorize("hasAnyRole('ADMIN', 'COORDENADOR')")
        public ResponseEntity<Void> criarUsuario(@RequestBody @Valid CriarUsuarioComandoDto criarUsuarioComandoDto) {
                criarUsuarioComando.execute(criarUsuarioComandoDto);
                return new ResponseEntity<>(HttpStatus.CREATED);
        }

        @GetMapping
        @PreAuthorize("hasAnyRole('ADMIN', 'COORDENADOR', 'PROFESSOR')")
        public ResponseEntity<List<ListarUsuariosResultadoItem>> listarUsuarios(ListarUsuariosParams params) {
                List<ListarUsuariosResultadoItem> resultado = listarUsuariosQuery.execute(params);
                if (resultado.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(resultado);
        }

        @GetMapping("/por-especialidade/{especialidadeId}")
        @PreAuthorize("hasAnyRole('ADMIN', 'COORDENADOR', 'PROFESSOR')")
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
        @PreAuthorize("hasAnyRole('ADMIN', 'COORDENADOR')")
        public ResponseEntity<Void> atualizarUsuario(@PathVariable Long id,
                        @RequestBody AtualizarUsuarioComandoDto dto) {
                atualizarUsuarioComando.execute(id, dto);
                return ResponseEntity.ok().build();
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'COORDENADOR')")
        public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
                desativarUsuarioComando.execute(id);
                return ResponseEntity.ok().build();
        }

        @GetMapping("/por-login/{login}")
        @PreAuthorize("hasAnyRole('ADMIN', 'COORDENADOR', 'PROFESSOR')")
        public ResponseEntity<ListarUsuariosResultadoItem> listarUsuarioPorLogin(@PathVariable String login) {
                var item = listarUsuarioPorLoginQuery.execute(login);
                return ResponseEntity.ok(item);
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'COORDENADOR', 'PROFESSOR')")
        public ResponseEntity<EncontraUsuarioItem> buscaUsuario(@PathVariable Long id, @RequestParam Perfil perfil, @RequestParam Optional<Long> especialidadeId) {
                return ResponseEntity.ok(buscaUsuarioPorIdQuery.execute(id, perfil, especialidadeId));
        }
}