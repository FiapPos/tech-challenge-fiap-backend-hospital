package com.fiap.techchallenge.agendamento_service.core.service;

import com.fiap.techchallenge.agendamento_service.core.client.UsuarioServiceClient;
import com.fiap.techchallenge.agendamento_service.core.dto.UsuarioDTO;
import com.fiap.techchallenge.agendamento_service.core.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.core.repository.ConsultaRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDateTime;
import java.util.List;

import static com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento.*;
import static java.util.stream.Collectors.joining;

@Component
public class SusAgendamentoBotService implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final UsuarioServiceClient service;
    private final FilaEsperaService filaEsperaService;
    private final ConsultaRepository consultaRepository;

    public SusAgendamentoBotService(UsuarioServiceClient service,
                                    FilaEsperaService filaEsperaService,
                                    ConsultaRepository consultaRepository) {
        this.service = service;
        this.filaEsperaService = filaEsperaService;
        this.consultaRepository = consultaRepository;
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public String getBotToken() {
        return "8312571764:AAHd8DskcfvLLt_lOd_kDKwdmI-aGLenEsw";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            enviaConfirmacaoConsulta(update);
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if (message_text.startsWith("/start")) enviaAcaoInicial(message_text, chat_id);
            else if (message_text.equals("/consultas")) listaProximasConsultas(chat_id);
        }
    }

    private void enviaConfirmacaoConsulta(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        if (callbackData.startsWith("CONFIRMAR_")) {
            Long id = Long.parseLong(callbackData.replace("CONFIRMAR_", ""));
            filaEsperaService.aceitarProposta(id);
            enviarMensagem(chatId, "Obrigado! Sua presença foi confirmada no sistema. Nos vemos lá! 👋");
        }
        else if (callbackData.startsWith("CANCELAR_")) {
            Long id = Long.parseLong(callbackData.replace("CANCELAR_", ""));
            filaEsperaService.recusarProposta(id);
            enviarMensagem(chatId, "Entendido. Sua consulta foi cancelada e a vaga foi liberada para outro paciente. 🤝");
        }
    }

    private void enviaAcaoInicial(String message_text, long chat_id) {
        String[] partes = message_text.split(" ");

        if (partes.length > 1) {
            String usuarioId = partes[1];
            service.vincularChatId(Long.valueOf(usuarioId), chat_id);
            enviarMensagem(chat_id, "✅ Olá! Eu sou o Zé Gotinha e esse é o serviço de notificações do SUS. Agora você receberá notificações de confirmação e lembretes por aqui! \n\n" +
                    "Lista de comandos: \n" +
                    "• /consultas: Listagem de consultas futuras");
        } else {
            enviarMensagem(chat_id, "Boas vindas ao assistente do SUS! Escaneie o QR Code na sua guia para ativar os lembretes.");
        }
    }

    private void listaProximasConsultas(long chat_id) {
        Long pacienteId = service.buscarUsuarioPorChatId(chat_id);

        if (pacienteId == null) {
            enviarMensagem(chat_id,
                    "Não encontrei seu cadastro vinculado a este chat. Escaneie o QR Code para ativar os lembretes.");
            return;
        }

        String corpoConsultas = consultaRepository
                .findAllFuturasConsultasByPacienteIdAndStatus(pacienteId, List.of(CRIADA, PENDENTE, ATUALIZADA), LocalDateTime.now())
                .stream()
                .map(Consulta::getTemplateDeMensagem)
                .collect(joining(". \n\n"));

        String consultas = "Essas são as suas consultas futuras pendentes: \n\n"
                + corpoConsultas
                + "\n\nVocê receberá notificação para confirmação 24hrs antes. \n\n";

        enviarMensagem(chat_id, corpoConsultas.isEmpty() ? "Não existem próximas consultas agendadas." : consultas);
    }

    public void enviarSolicitacaoConfirmacaoParaPacienteNaFilaDeEspera(Long pacienteId, Long filaEsperaId, Consulta consulta) {
        if (pacienteId == null) return;

        UsuarioDTO paciente = service.buscarUsuarioPorId(pacienteId, "PACIENTE");
        if (paciente == null) return;

        long chatId = paciente.getChatId();
        if (chatId == 0L) return;

        enviarSolicitacaoConfirmacaoParaFilaDeEspera(chatId, filaEsperaId, consulta);
    }

    private void enviarSolicitacaoConfirmacaoParaFilaDeEspera(long chatId, Long filaEsperaId, Consulta consulta) {
        InlineKeyboardButton botaoSim = InlineKeyboardButton.builder()
                .text("✅ Confirmar Presença")
                .callbackData("CONFIRMAR_" + filaEsperaId)
                .build();
        InlineKeyboardButton botaoNao = InlineKeyboardButton.builder()
                .text("❌ Não poderei ir")
                .callbackData("CANCELAR_" + filaEsperaId)
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow(botaoSim, botaoNao);
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboardRow(row)
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("🔔 *VOCÊ TEM UMA NOVA CONSULTA! *\n\n" + consulta.getTemplateDeMensagem() + ". \n\nVocê confirma sua presença?")
                .parseMode("Markdown") // Permite negrito
                .replyMarkup(markup)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void enviarConfirmacao(Consulta consulta) {
        UsuarioDTO paciente = service.buscarUsuarioPorId(consulta.getPacienteId(), "PACIENTE");
        enviarMensagem(paciente.getChatId(), "🔔 *LEMBRETE DE CONSULTA*\n\n" + consulta.getTemplateDeMensagem() + " foi confirmada.");
    }

    public void enviaAtualizacao(Consulta consulta) {
        UsuarioDTO paciente = service.buscarUsuarioPorId(consulta.getPacienteId(), "PACIENTE");
        enviarMensagem(paciente.getChatId(), "🔔 *LEMBRETE DE CONSULTA*\n\n" + consulta.getTemplateDeMensagem() + " foi atualizada.");
    }

    public void enviaCancelamento(Consulta consulta) {
        UsuarioDTO paciente = service.buscarUsuarioPorId(consulta.getPacienteId(), "PACIENTE");
        enviarMensagem(paciente.getChatId(), "🔔 *LEMBRETE DE CONSULTA*\n\n" + consulta.getTemplateDeMensagem() + " foi cancelada.");
    }

    private void enviarMensagem(long chatId, String texto) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(texto)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Bot do SUS registrado com sucesso! Ativo: " + botSession.isRunning());
    }
}