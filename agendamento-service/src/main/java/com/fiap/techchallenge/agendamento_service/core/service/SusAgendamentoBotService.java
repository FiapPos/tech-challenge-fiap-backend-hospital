package com.fiap.techchallenge.agendamento_service.core.service;

import com.fiap.techchallenge.agendamento_service.core.client.UsuarioServiceClient;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class SusAgendamentoBotService implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final UsuarioServiceClient service;

    public SusAgendamentoBotService(UsuarioServiceClient service) {
        this.service = service;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if (message_text.startsWith("/start")) mendaMensagemDeInicio(message_text, chat_id);
            else if (message_text.equals("/consultas")) enviarMensagem(chat_id, "Você tem uma consulta de Odontologia amanhã às 14h no Posto Central.");
        }
    }

    private void mendaMensagemDeInicio(String message_text, long chat_id) {
        String[] partes = message_text.split(" ");

        if (partes.length > 1) {
            String usuarioId = partes[1];

            service.vincularChatId(Long.valueOf(usuarioId), chat_id);

            enviarMensagem(chat_id, "✅ Olá! Esse é o serviço de notificações do SUS. Recebemos sua solicitação de agendamento. \n\n" +
                    "Agora você receberá notificações de confirmação e lembretes por aqui!");
        } else {
            enviarMensagem(chat_id, "Bem-vindo ao assistente do SUS! Escaneie o QR Code na sua guia para ativar os lembretes.");
        }
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