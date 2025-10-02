package com.fiap.techchallenge.usuario_service.infrastructure.api.controllers;

import com.fiap.techchallenge.usuario_service.core.domain.entities.Usuario;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.login.AtualizaCredenciaisComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.login.AutenticaJwtComando;
import com.fiap.techchallenge.usuario_service.core.domain.usecases.login.AutenticaLoginComando;
import com.fiap.techchallenge.usuario_service.core.dtos.login.AtualizaCredenciaisComandoDto;
import com.fiap.techchallenge.usuario_service.core.dtos.login.CredenciaisUsuarioDto;
import com.fiap.techchallenge.usuario_service.core.utils.ValidaConfirmacaoDeSenha;
import com.fiap.techchallenge.usuario_service.core.utils.doc.LoginControllerDoc;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController implements LoginControllerDoc {

    private final AutenticaJwtComando autenticaJwtComando;
    private final AutenticaLoginComando autenticaLoginComando;
    private final AtualizaCredenciaisComando atualizaCredenciaisComando;
    private final ValidaConfirmacaoDeSenha validaConfirmacaoDeSenha;

    public LoginController(AutenticaJwtComando autenticaJwtComando,
            AutenticaLoginComando autenticaLoginComando,
            AtualizaCredenciaisComando atualizaCredenciaisComando,
            ValidaConfirmacaoDeSenha validaConfirmacaoDeSenha) {
        this.autenticaJwtComando = autenticaJwtComando;
        this.autenticaLoginComando = autenticaLoginComando;
        this.atualizaCredenciaisComando = atualizaCredenciaisComando;
        this.validaConfirmacaoDeSenha = validaConfirmacaoDeSenha;
    }

    @InitBinder("atualizaCredenciaisComandoDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(validaConfirmacaoDeSenha);
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid CredenciaisUsuarioDto credentials,
            BindingResult bindingResult) throws Exception {

        if (bindingResult != null && bindingResult.hasErrors()) {
            Map<String, String> erros = new HashMap<>();
            bindingResult.getFieldErrors()
                    .forEach(error -> erros.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(erros);
        }

        Usuario user = autenticaLoginComando.login(credentials);
        // If perfil was not provided, choose the first profile associated to the user
        com.fiap.techchallenge.usuario_service.core.enums.Perfil perfil = credentials.perfil();
        if (perfil == null) {
            perfil = user.getPerfis().stream()
                    .map(p -> p.getPerfil())
                    .findFirst()
                    .orElse(com.fiap.techchallenge.usuario_service.core.enums.Perfil.PACIENTE);
        }

        String token = autenticaJwtComando.createToken(user, perfil);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @Transactional
    @PutMapping("/login/atualiza-senha")
    public ResponseEntity<Map<String, String>> atualizaSenha(
            @RequestBody @Valid AtualizaCredenciaisComandoDto atualizaCredenciaisComandoDto,
            BindingResult bindingResult) {

        BeanPropertyBindingResult manualBinding = null;
        if (bindingResult == null || !bindingResult.hasErrors()) {
            manualBinding = new BeanPropertyBindingResult(atualizaCredenciaisComandoDto,
                    "atualizaCredenciaisComandoDto");
            validaConfirmacaoDeSenha.validate(atualizaCredenciaisComandoDto, manualBinding);
        }

        BindingResult effective = (bindingResult != null && bindingResult.hasErrors()) ? bindingResult : manualBinding;

        if (effective != null && effective.hasErrors()) {
            Map<String, String> erros = new HashMap<>();
            for (FieldError error : effective.getFieldErrors()) {
                erros.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(erros);
        }

        atualizaCredenciaisComando.execute(atualizaCredenciaisComandoDto);
        return ResponseEntity.ok().build();
    }
}
