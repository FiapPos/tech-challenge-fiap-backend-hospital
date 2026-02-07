package com.fiap.techchallenge.agendamento_service.configuration;

import com.fiap.techchallenge.agendamento_service.core.service.FilaEsperaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class FilaEsperaScheduler {

    private final FilaEsperaService filaEsperaService;

    @Scheduled(cron = "0 */5 * * * *")
    public void alocarPacientesDaFila() {
        log.info("Iniciando alocação automática de pacientes da fila de espera");
        try {
            int alocados = filaEsperaService.alocarProximosDaFila();
            log.info("Alocação automática concluída - pacientes alocados: {}", alocados);
        } catch (Exception e) {
            log.error("Erro ao alocar pacientes da fila: {}", e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void verificarPropostasExpiradas() {
        log.info("Verificando propostas expiradas (não respondidas em 24h)");
        try {
            filaEsperaService.expirarPropostasAntigas();
            log.info("Verificação de propostas expiradas concluída");
        } catch (Exception e) {
            log.error("Erro ao verificar propostas expiradas: {}", e.getMessage());
        }
    }
}
