package com.fiap.techchallenge.usuario_service.core.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();

            if (defaultMessage != null && defaultMessage.startsWith("{") && defaultMessage.endsWith("}")) {
                String messageKey = defaultMessage.substring(1, defaultMessage.length() - 1);
                defaultMessage = messageSource.getMessage(messageKey, null, messageKey,
                        LocaleContextHolder.getLocale());
            }

            errors.put(fieldName, defaultMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        String mensagem = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(mensagem));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        String mensagem = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(mensagem));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String mensagemPadrao = messageSource.getMessage("violacao.integridade.dados", null,
                "Violação de integridade dos dados", LocaleContextHolder.getLocale());

        String detalhe = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        String mensagem = mensagemPadrao;

        if (detalhe != null) {
            String lower = detalhe.toLowerCase();
            if (lower.contains("usuarios_email_key")) {
                mensagem = messageSource.getMessage("email.duplicado", null, LocaleContextHolder.getLocale());
            } else if (lower.contains("usuarios_login_key")) {
                mensagem = messageSource.getMessage("login.duplicado", null, LocaleContextHolder.getLocale());
            } else if (lower.contains("usuarios_cpf_key")) {
                mensagem = messageSource.getMessage("cpf.duplicado", null, LocaleContextHolder.getLocale());
            } else if (lower.contains("fk_usuarios_perfil") || lower.contains("usuarios_perfil_id_fkey")) {
                mensagem = messageSource.getMessage("perfil.obrigatorio", null, LocaleContextHolder.getLocale());
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(mensagem));
    }

}