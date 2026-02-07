package com.fiap.techchallenge.agendamento_service.core.enums;

/**
 * Status da entrada na fila de espera
 */
public enum EStatusFilaEspera {
    AGUARDANDO,      // Aguardando vaga
    NOTIFICADO,      // Notificado sobre vaga disponível
    ACEITO,          // Paciente aceitou a proposta
    RECUSADO,        // Paciente recusou a proposta
    EXPIRADO,        // Proposta expirou (sem resposta em 24h)
    REMOVIDO         // Removido da fila pelo paciente
}
