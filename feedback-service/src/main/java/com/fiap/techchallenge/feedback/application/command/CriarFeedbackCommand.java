package com.fiap.techchallenge.feedback.application.command;

import com.fiap.techchallenge.feedback.application.dto.FeedbackRequestDTO;
import com.fiap.techchallenge.feedback.application.dto.FeedbackResponseDTO;
import com.fiap.techchallenge.feedback.core.domain.model.Feedback;
import com.fiap.techchallenge.feedback.core.domain.model.StatusFeedback;

import com.fiap.techchallenge.feedback.core.domain.repository.FeedbackRepository;
import com.fiap.techchallenge.feedback.infrastructure.messaging.FeedbackEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CriarFeedbackCommand {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackEventProducer eventProducer;

    @Transactional
    public FeedbackResponseDTO executar(FeedbackRequestDTO requestDTO, Long estudanteId) {
        registrarLog(estudanteId, requestDTO.getAulaId());

        Feedback feedback = construirFeedback(requestDTO, estudanteId);
        Feedback savedFeedback = salvarFeedback(feedback);
        publicarEvento(savedFeedback);

        return converterParaDTO(savedFeedback);
    }

    private void registrarLog(Long estudanteId, Long aulaId) {
        log.info("Criando feedback para estudante {} na aula {}", estudanteId, aulaId);
    }

    private Feedback construirFeedback(FeedbackRequestDTO requestDTO, Long estudanteId) {
        Feedback feedback = new Feedback();
        feedback.setAulaId(requestDTO.getAulaId());
        feedback.setCursoId(requestDTO.getCursoId());
        feedback.setEstudanteId(estudanteId);
        feedback.setProfessorId(requestDTO.getProfessorId());
        feedback.setNota(requestDTO.getNota());
        feedback.setComentario(requestDTO.getComentario());
        feedback.setCategoria(requestDTO.getCategoria());
        feedback.setAnonimo(requestDTO.getAnonimo() != null ? requestDTO.getAnonimo() : false);
        feedback.setStatus(StatusFeedback.PENDENTE);
        feedback.setDataCriacao(LocalDateTime.now());
        return feedback;
    }

    private Feedback salvarFeedback(Feedback feedback) {
        Feedback savedFeedback = feedbackRepository.save(feedback);
        log.info("Feedback {} criado com sucesso", savedFeedback.getId());
        return savedFeedback;
    }

    private void publicarEvento(Feedback feedback) {
        eventProducer.publicarFeedbackCriado(feedback);
    }

    private FeedbackResponseDTO converterParaDTO(Feedback feedback) {
        return FeedbackResponseDTO.fromEntity(feedback);
    }
}
