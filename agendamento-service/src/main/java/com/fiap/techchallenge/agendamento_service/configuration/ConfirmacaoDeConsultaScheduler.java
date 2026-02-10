package com.fiap.techchallenge.agendamento_service.configuration;

import com.fiap.techchallenge.agendamento_service.core.repository.ConsultaRepository;
import com.fiap.techchallenge.agendamento_service.core.service.SusAgendamentoBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento.PENDENTE;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ConfirmacaoDeConsultaScheduler {

    private final ConsultaRepository consultaRepository;
    private final SusAgendamentoBotService susAgendamentoBotService;

    /**
     * Scheduler para enviar notificações de confirmação para
     * as consultas que irão ocorrer dentro das
     * próximas 24hrs. Caso seja negada, a consulta
     * é redirecionada para o próximo interessado
     * da fila de prioridades.
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void enviaConfirmacaoDeConsultas() {
        log.info("Enviando confirmacao para consulta dentro das 24hrs");
        try {
            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime amanhaMesmaHora = agora.plusHours(24);

            consultaRepository.buscarConsultasNasProximas24Horas(
                    PENDENTE,
                    amanhaMesmaHora.minusMinutes(4),
                    amanhaMesmaHora
            ).forEach(susAgendamentoBotService::enviarSolicitacaoConfirmacaoParaPaciente);

            log.info("Confirmações enviadas com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao enviar solicitações: {}", e.getMessage());
        }
    }
}
