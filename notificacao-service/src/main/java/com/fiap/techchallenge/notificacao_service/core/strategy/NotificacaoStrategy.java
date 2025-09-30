package com.fiap.techchallenge.notificacao_service.core.strategy;

import com.fiap.techchallenge.notificacao_service.core.dto.DadosAgendamento;

/**
 * Interface que define a estratégia para processamento de notificações.
 * Implementa o Strategy Pattern para diferentes tipos de notificação.
 */
public interface NotificacaoStrategy {
    
    /**
     * Processa a notificação específica do tipo implementado.
     * 
     * @param agendamento dados do agendamento
     * @return mensagem da notificação processada
     */
    String processar(DadosAgendamento agendamento);
    
    /**
     * Verifica se esta estratégia é aplicável para o agendamento fornecido.
     * 
     * @param agendamento dados do agendamento
     * @return true se a estratégia é aplicável
     */
    boolean isAplicavel(DadosAgendamento agendamento);
}
