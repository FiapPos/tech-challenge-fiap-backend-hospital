package com.fiap.techchallenge.feedback.core.controller;

import com.fiap.techchallenge.feedback.application.command.*;
import com.fiap.techchallenge.feedback.application.dto.*;
import com.fiap.techchallenge.feedback.application.query.*;
import com.fiap.techchallenge.feedback.core.domain.model.StatusFeedback;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Slf4j
public class FeedbackController {

    // Commands
    private final CriarFeedbackCommand criarFeedbackCommand;
    private final ResponderFeedbackCommand responderFeedbackCommand;
    private final AtualizarStatusFeedbackCommand atualizarStatusFeedbackCommand;

    // Queries
    private final BuscarFeedbackPorIdQuery buscarFeedbackPorIdQuery;
    private final ListarFeedbacksPorAulaQuery listarFeedbacksPorAulaQuery;
    private final ListarFeedbacksPorCursoQuery listarFeedbacksPorCursoQuery;
    private final ListarFeedbacksPorEstudanteQuery listarFeedbacksPorEstudanteQuery;
    private final ListarFeedbacksPorProfessorQuery listarFeedbacksPorProfessorQuery;
    private final ListarFeedbacksCriticosQuery listarFeedbacksCriticosQuery;
    private final CalcularMediaNotaAulaQuery calcularMediaNotaAulaQuery;
    private final CalcularMediaNotaCursoQuery calcularMediaNotaCursoQuery;
    private final CalcularMediaNotaProfessorQuery calcularMediaNotaProfessorQuery;

    @PostMapping
    @PreAuthorize("hasRole('ESTUDANTE')")
    public ResponseEntity<FeedbackResponseDTO> criarFeedback(
            @Valid @RequestBody FeedbackRequestDTO request,
            @RequestHeader("X-User-Id") Long estudanteId) {
        log.info("Recebendo requisição para criar feedback da aula {}", request.getAulaId());
        FeedbackResponseDTO response = criarFeedbackCommand.executar(request, estudanteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'COORDENADOR', 'ADMIN')")
    public ResponseEntity<FeedbackResponseDTO> buscarPorId(@PathVariable Long id) {
        FeedbackResponseDTO response = buscarFeedbackPorIdQuery.executar(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/aula/{aulaId}")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'COORDENADOR', 'ADMIN')")
    public ResponseEntity<List<FeedbackResponseDTO>> listarPorAula(@PathVariable Long aulaId) {
        List<FeedbackResponseDTO> feedbacks = listarFeedbacksPorAulaQuery.executar(aulaId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/curso/{cursoId}")
    @PreAuthorize("hasAnyRole('COORDENADOR', 'ADMIN')")
    public ResponseEntity<List<FeedbackResponseDTO>> listarPorCurso(@PathVariable Long cursoId) {
        List<FeedbackResponseDTO> feedbacks = listarFeedbacksPorCursoQuery.executar(cursoId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/estudante/{estudanteId}")
    @PreAuthorize("hasAnyRole('COORDENADOR', 'ADMIN')")
    public ResponseEntity<List<FeedbackResponseDTO>> listarPorEstudante(@PathVariable Long estudanteId) {
        List<FeedbackResponseDTO> feedbacks = listarFeedbacksPorEstudanteQuery.executar(estudanteId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/professor/{professorId}")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'COORDENADOR', 'ADMIN')")
    public ResponseEntity<List<FeedbackResponseDTO>> listarPorProfessor(@PathVariable Long professorId) {
        List<FeedbackResponseDTO> feedbacks = listarFeedbacksPorProfessorQuery.executar(professorId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/criticos")
    @PreAuthorize("hasAnyRole('COORDENADOR', 'ADMIN')")
    public ResponseEntity<List<FeedbackResponseDTO>> listarFeedbacksCriticos() {
        List<FeedbackResponseDTO> feedbacks = listarFeedbacksCriticosQuery.executar();
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping("/{id}/responder")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'COORDENADOR', 'ADMIN')")
    public ResponseEntity<FeedbackResponseDTO> responderFeedback(
            @PathVariable Long id,
            @Valid @RequestBody RespostaFeedbackDTO respostaDTO,
            @RequestHeader("X-User-Id") Long adminId) {
        log.info("Usuário {} respondendo feedback {}", adminId, id);
        FeedbackResponseDTO response = responderFeedbackCommand.executar(id, respostaDTO, adminId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('COORDENADOR', 'ADMIN')")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusFeedback status) {
        atualizarStatusFeedbackCommand.executar(id, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/aula/{aulaId}/media")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'COORDENADOR', 'ADMIN')")
    public ResponseEntity<MediaNotaAulaResponseDTO> calcularMediaAula(@PathVariable Long aulaId) {
        MediaNotaAulaResponseDTO response = calcularMediaNotaAulaQuery.executar(aulaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/curso/{cursoId}/media")
    @PreAuthorize("hasAnyRole('COORDENADOR', 'ADMIN')")
    public ResponseEntity<MediaNotaCursoResponseDTO> calcularMediaCurso(@PathVariable Long cursoId) {
        MediaNotaCursoResponseDTO response = calcularMediaNotaCursoQuery.executar(cursoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/professor/{professorId}/media")
    @PreAuthorize("hasAnyRole('COORDENADOR', 'ADMIN')")
    public ResponseEntity<MediaNotaProfessorResponseDTO> calcularMediaProfessor(@PathVariable Long professorId) {
        MediaNotaProfessorResponseDTO response = calcularMediaNotaProfessorQuery.executar(professorId);
        return ResponseEntity.ok(response);
    }
}
