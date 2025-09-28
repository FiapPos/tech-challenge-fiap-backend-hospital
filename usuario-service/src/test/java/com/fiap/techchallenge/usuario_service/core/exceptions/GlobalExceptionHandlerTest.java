package com.fiap.techchallenge.usuario_service.core.exceptions;

import com.fiap.techchallenge.usuario_service.core.exceptions.BadRequestException;
import com.fiap.techchallenge.usuario_service.core.exceptions.ErrorResponse;
import com.fiap.techchallenge.usuario_service.core.exceptions.ForbiddenException;
import com.fiap.techchallenge.usuario_service.core.exceptions.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveTratarBadRequestException() {
        String mensagemKey = "erro.badrequest";
        String mensagemTraduzida = "Mensagem traduzida";
        when(messageSource.getMessage(eq(mensagemKey), eq(null), any())).thenReturn(mensagemTraduzida);
        BadRequestException ex = new BadRequestException(mensagemKey);

        ResponseEntity<ErrorResponse> response = handler.handleBadRequestException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(mensagemTraduzida, Objects.requireNonNull(response.getBody()).getMensagem());
    }

    @Test
    void deveTratarForbiddenException() {
        String mensagemKey = "erro.forbidden";
        String mensagemTraduzida = "Acesso negado";
        when(messageSource.getMessage(eq(mensagemKey), eq(null), any())).thenReturn(mensagemTraduzida);
        ForbiddenException ex = new ForbiddenException(mensagemKey);

        ResponseEntity<ErrorResponse> response = handler.handleForbiddenException(ex);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(mensagemTraduzida, Objects.requireNonNull(response.getBody()).getMensagem());
    }

    @Test
    void deveTratarIllegalArgumentException() {
        String mensagem = "Argumento ilegal fornecido";
        IllegalArgumentException ex = new IllegalArgumentException(mensagem);

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(mensagem, Objects.requireNonNull(response.getBody()).getMensagem());
    }

    @Test
    void deveTratarIllegalArgumentExceptionComMensagemNula() {
        IllegalArgumentException ex = new IllegalArgumentException();

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(Objects.requireNonNull(response.getBody()).getMensagem());
    }

    @Test
    void deveTratarMethodArgumentNotValidExceptionComMensagemTraduzida() {
        String fieldName = "email";
        String messageKeyWrapped = "{validation.email.invalid}";
        String messageKey = "validation.email.invalid";
        String translatedMessage = "Email inválido";

        FieldError fieldError = new FieldError("object", fieldName, messageKeyWrapped);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        when(messageSource.getMessage(eq(messageKey), eq(null), eq(messageKey), any())).thenReturn(translatedMessage);

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertEquals(translatedMessage, errors.get(fieldName));
    }

    @Test
    void deveTratarMethodArgumentNotValidExceptionComMensagemPadrao() {
        String fieldName = "nome";
        String defaultMessage = "Nome é obrigatório";

        FieldError fieldError = new FieldError("object", fieldName, defaultMessage);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertEquals(defaultMessage, errors.get(fieldName));
    }

    @Test
    void deveTratarMethodArgumentNotValidExceptionComMensagemNula() {
        String fieldName = "idade";
        String defaultMessage = null;

        FieldError fieldError = new FieldError("object", fieldName, defaultMessage);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertNull(errors.get(fieldName));
    }

    @Test
    void deveTratarMethodArgumentNotValidExceptionComMultiplosErros() {
        FieldError error1 = new FieldError("object", "email", "{validation.email.invalid}");
        FieldError error2 = new FieldError("object", "senha", "Senha é obrigatória");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(error1, error2));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        when(messageSource.getMessage(eq("validation.email.invalid"), eq(null), eq("validation.email.invalid"), any()))
                .thenReturn("Email inválido");

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertEquals("Email inválido", errors.get("email"));
        assertEquals("Senha é obrigatória", errors.get("senha"));
        assertEquals(2, errors.size());
    }

    @Test
    void deveTratarMethodArgumentNotValidExceptionComListaVaziaDeErros() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertTrue(errors.isEmpty());
    }

    @Test
    void deveTratarDataIntegrityViolationComMensagensEspecificas() {
        // email duplicado
        DataIntegrityViolationException exEmail = new DataIntegrityViolationException("violação: usuarios_email_key");
        when(messageSource.getMessage(eq("violacao.integridade.dados"), any(), anyString(), any()))
                .thenReturn("Violação de integridade dos dados");
        when(messageSource.getMessage(eq("email.duplicado"), any(), any())).thenReturn("E-mail já cadastrado");
        ResponseEntity<ErrorResponse> r1 = handler.handleDataIntegrityViolation(exEmail);
        assertEquals(HttpStatus.BAD_REQUEST, r1.getStatusCode());
        assertEquals("E-mail já cadastrado", Objects.requireNonNull(r1.getBody()).getMensagem());

        // login duplicado
        DataIntegrityViolationException exLogin = new DataIntegrityViolationException("violação: usuarios_login_key");
        when(messageSource.getMessage(eq("login.duplicado"), any(), any())).thenReturn("Login já cadastrado");
        ResponseEntity<ErrorResponse> r2 = handler.handleDataIntegrityViolation(exLogin);
        assertEquals("Login já cadastrado", Objects.requireNonNull(r2.getBody()).getMensagem());

        // cpf duplicado
        DataIntegrityViolationException exCpf = new DataIntegrityViolationException("violação: usuarios_cpf_key");
        when(messageSource.getMessage(eq("cpf.duplicado"), any(), any())).thenReturn("CPF já cadastrado");
        ResponseEntity<ErrorResponse> r3 = handler.handleDataIntegrityViolation(exCpf);
        assertEquals("CPF já cadastrado", Objects.requireNonNull(r3.getBody()).getMensagem());

        // perfil obrigatório
        DataIntegrityViolationException exPerfil = new DataIntegrityViolationException("violação: fk_usuarios_perfil");
        when(messageSource.getMessage(eq("perfil.obrigatorio"), any(), any())).thenReturn("Perfil é obrigatório");
        ResponseEntity<ErrorResponse> r4 = handler.handleDataIntegrityViolation(exPerfil);
        assertEquals("Perfil é obrigatório", Objects.requireNonNull(r4.getBody()).getMensagem());

        // mensagem padrão quando não mapeado
        DataIntegrityViolationException exOutro = new DataIntegrityViolationException("outra violação qualquer");
        ResponseEntity<ErrorResponse> r5 = handler.handleDataIntegrityViolation(exOutro);
        assertEquals("Violação de integridade dos dados", Objects.requireNonNull(r5.getBody()).getMensagem());
    }
}