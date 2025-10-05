package com.fiap.techchallenge.historico_service.core.controller;

import com.fiap.techchallenge.historico_service.core.entity.HistoricoMedico;
import com.fiap.techchallenge.historico_service.core.enums.EStatusAgendamento;
import com.fiap.techchallenge.historico_service.core.service.HistoricoMedicoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HistoricoMedicoGraphQLController {

    private final HistoricoMedicoService historicoMedicoService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @QueryMapping
    public List<HistoricoMedico> todosAtendimentosPaciente(@Argument Long pacienteId) {
        log.info("GraphQL Query: Buscando todos os atendimentos do paciente ID: {}", pacienteId);
        return historicoMedicoService.buscarTodosAtendimentosPaciente(pacienteId);
    }

    @QueryMapping
    public List<HistoricoMedico> atendimentosFuturosPaciente(@Argument Long pacienteId) {
        log.info("GraphQL Query: Buscando atendimentos futuros do paciente ID: {}", pacienteId);
        return historicoMedicoService.buscarAtendimentosFuturosPaciente(pacienteId);
    }

    @QueryMapping
    public List<HistoricoMedico> atendimentosPassadosPaciente(@Argument Long pacienteId) {
        log.info("GraphQL Query: Buscando atendimentos passados do paciente ID: {}", pacienteId);
        return historicoMedicoService.buscarAtendimentosPassadosPaciente(pacienteId);
    }

    @QueryMapping
    public List<HistoricoMedico> atendimentosPorStatus(@Argument Long pacienteId, @Argument EStatusAgendamento status) {
        log.info("GraphQL Query: Buscando atendimentos do paciente ID: {} com status: {}", pacienteId, status);
        return historicoMedicoService.buscarAtendimentosPorStatus(pacienteId, status);
    }

    @QueryMapping
    public List<HistoricoMedico> atendimentosPorPeriodo(@Argument Long pacienteId,
                                                        @Argument String dataInicio,
                                                        @Argument String dataFim) {
        log.info("GraphQL Query: Buscando atendimentos do paciente ID: {} entre {} e {}",
                pacienteId, dataInicio, dataFim);

        LocalDateTime inicio = LocalDateTime.parse(dataInicio, formatter);
        LocalDateTime fim = LocalDateTime.parse(dataFim, formatter);

        return historicoMedicoService.buscarAtendimentosPorPeriodo(pacienteId, inicio, fim);
    }

    @QueryMapping
    public List<HistoricoMedico> atendimentosPorMedico(@Argument Long medicoId) {
        log.info("GraphQL Query: Buscando atendimentos do médico ID: {}", medicoId);
        return historicoMedicoService.buscarAtendimentosPorMedico(medicoId);
    }

    @QueryMapping
    public List<HistoricoMedico> atendimentosFuturosPorMedico(@Argument Long medicoId) {
        log.info("GraphQL Query: Buscando atendimentos futuros do médico ID: {}", medicoId);
        return historicoMedicoService.buscarAtendimentosFuturosPorMedico(medicoId);
    }

    @QueryMapping
    public List<HistoricoMedico> atendimentosPorHospital(@Argument Long hospitalId) {
        log.info("GraphQL Query: Buscando atendimentos do hospital ID: {}", hospitalId);
        return historicoMedicoService.buscarAtendimentosPorHospital(hospitalId);
    }
}
