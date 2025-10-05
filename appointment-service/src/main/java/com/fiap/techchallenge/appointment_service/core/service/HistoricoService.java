package com.fiap.techchallenge.appointment_service.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.appointment_service.core.client.historico.HistoricoGraphqlClient;
import com.fiap.techchallenge.appointment_service.core.dto.response.HistoricoMedicoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoGraphqlClient graphqlClient;
    private final ObjectMapper mapper;

    @Value("${historico-service.url:http://localhost:8081}")
    private String historicoBaseUrl;

    public List<HistoricoMedicoDto> buscarTodosAtendimentosPaciente(Long pacienteId) {
        String query = "query($pacienteId: Long) { todosAtendimentosPaciente(pacienteId: $pacienteId) { id dataHora status medicoId pacienteId descricao } }";

        JsonNode root = graphqlClient.execute(query, Map.of("pacienteId", pacienteId), historicoBaseUrl);
        if (root == null)
            return List.of();

        if (root.has("errors")) {
            throw new RuntimeException("GraphQL errors: " + root.get("errors").toString());
        }

        JsonNode data = root.get("data");
        if (data == null || data.isNull())
            return List.of();

        JsonNode arr = data.get("todosAtendimentosPaciente");
        if (arr == null || arr.isNull())
            return List.of();

        List<HistoricoMedicoDto> out = new ArrayList<>();
        for (JsonNode node : arr) {
            out.add(mapper.convertValue(node, HistoricoMedicoDto.class));
        }
        return out;
    }

    public List<HistoricoMedicoDto> buscarAtendimentosFuturosPaciente(Long pacienteId) {
        String query = "query($pacienteId: Long) { atendimentosFuturosPaciente(pacienteId: $pacienteId) { id agendamentoId pacienteId nomePaciente nomeMedico nomeHospital enderecoHospital especializacao statusAgendamento dataHoraAgendamento criadoEm atualizadoEm } }";

        JsonNode root = graphqlClient.execute(query, Map.of("pacienteId", pacienteId), historicoBaseUrl);
        if (root == null)
            return List.of();

        if (root.has("errors")) {
            throw new RuntimeException("GraphQL errors: " + root.get("errors").toString());
        }

        JsonNode data = root.get("data");
        if (data == null || data.isNull())
            return List.of();

        JsonNode arr = data.get("atendimentosFuturosPaciente");
        if (arr == null || arr.isNull())
            return List.of();

        List<HistoricoMedicoDto> out = new ArrayList<>();
        for (JsonNode node : arr) {
            out.add(mapper.convertValue(node, HistoricoMedicoDto.class));
        }
        return out;
    }

    public List<HistoricoMedicoDto> buscarAtendimentosPorMedico(Long medicoId) {
        String query = "query($medicoId: Long) { atendimentosPorMedico(medicoId: $medicoId) { id agendamentoId pacienteId nomePaciente nomeMedico nomeHospital enderecoHospital especializacao statusAgendamento dataHoraAgendamento criadoEm atualizadoEm } }";

        JsonNode root = graphqlClient.execute(query, Map.of("medicoId", medicoId), historicoBaseUrl);
        if (root == null)
            return List.of();

        if (root.has("errors")) {
            throw new RuntimeException("GraphQL errors: " + root.get("errors").toString());
        }

        JsonNode data = root.get("data");
        if (data == null || data.isNull())
            return List.of();

        JsonNode arr = data.get("atendimentosPorMedico");
        if (arr == null || arr.isNull())
            return List.of();

        List<HistoricoMedicoDto> out = new ArrayList<>();
        for (JsonNode node : arr) {
            out.add(mapper.convertValue(node, HistoricoMedicoDto.class));
        }
        return out;
    }

    public List<HistoricoMedicoDto> buscarAtendimentosFuturosPorMedico(Long medicoId) {
        String query = "query($medicoId: Long) { atendimentosFuturosPorMedico(medicoId: $medicoId) { id agendamentoId pacienteId nomePaciente nomeMedico nomeHospital enderecoHospital especializacao statusAgendamento dataHoraAgendamento criadoEm atualizadoEm } }";

        JsonNode root = graphqlClient.execute(query, Map.of("medicoId", medicoId), historicoBaseUrl);
        if (root == null)
            return List.of();

        if (root.has("errors")) {
            throw new RuntimeException("GraphQL errors: " + root.get("errors").toString());
        }

        JsonNode data = root.get("data");
        if (data == null || data.isNull())
            return List.of();

        JsonNode arr = data.get("atendimentosFuturosPorMedico");
        if (arr == null || arr.isNull())
            return List.of();

        List<HistoricoMedicoDto> out = new ArrayList<>();
        for (JsonNode node : arr) {
            out.add(mapper.convertValue(node, HistoricoMedicoDto.class));
        }
        return out;
    }
}
