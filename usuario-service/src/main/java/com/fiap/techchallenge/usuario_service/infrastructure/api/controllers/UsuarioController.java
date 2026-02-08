package com.fiap.techchallenge.usuario_service.infrastructure.api.controllers;

import com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario.AtualizarUsuarioComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario.CriarUsuarioComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario.DesativarUsuarioComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.usuario.GeraQrCodeComando;
import com.fiap.techchallenge.usuario_service.core.dtos.usuario.AtualizarUsuarioComandoDto;
import com.fiap.techchallenge.usuario_service.core.dtos.usuario.CriarUsuarioComandoDto;
import com.fiap.techchallenge.usuario_service.core.enums.Perfil;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.usuario.EncontraUsuarioItem;
import com.fiap.techchallenge.usuario_service.core.queries.usuario.*;
import com.fiap.techchallenge.usuario_service.core.queries.params.ListarUsuariosParams;
import com.fiap.techchallenge.usuario_service.core.queries.resultadoItem.usuario.ListarUsuariosResultadoItem;
import com.fiap.techchallenge.usuario_service.core.utils.doc.UsuarioControllerDoc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

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
        private final BuscaUsuarioPorChatIdQuery buscaUsuarioPorChatIdQuery;
        private final GeraQrCodeComando geraQrCodeComando;

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

        @GetMapping("/por-login/{login}")
        public ResponseEntity<ListarUsuariosResultadoItem> listarUsuarioPorLogin(@PathVariable String login) {
                var item = listarUsuarioPorLoginQuery.execute(login);
                return ResponseEntity.ok(item);
        }

        @GetMapping("/{id}")
        public ResponseEntity<EncontraUsuarioItem> buscaUsuario(@PathVariable Long id, @RequestParam Perfil perfil, @RequestParam Optional<Long> especialidadeId) {
                return ResponseEntity.ok(buscaUsuarioPorIdQuery.execute(id, perfil, especialidadeId));
        }

        @GetMapping("/por-chat/{chatId}")
        public ResponseEntity<Long> buscaUsuarioPorChatId(@PathVariable Long chatId) {
                return ResponseEntity.ok(buscaUsuarioPorChatIdQuery.execute(chatId));
        }


        @GetMapping(value = "/{id}/qrCode", produces = IMAGE_PNG_VALUE)
        public ResponseEntity<byte[]> geraQrCode(@PathVariable Long id) {
                return ResponseEntity.ok(geraQrCodeComando.geraQrCode(id));
        }

        @PutMapping("/atualiza-chat-id/{id}")
        void vincularChatId(@PathVariable Long id, @RequestParam("chatId") Long chatId) {
                atualizarUsuarioComando.adicionaChatId(id, chatId);
        }
}