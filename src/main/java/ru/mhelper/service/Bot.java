package ru.mhelper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mhelper.exceptions.ErrorGettingCode;
import ru.mhelper.exceptions.TelegramBotServiceException;
import ru.mhelper.service.code.TelegramCodeService;

import static ru.mhelper.exceptions.TelegramBotServiceException.ERROR_SEND_MESSAGE;

@Service
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    private static final String NUMBER_MESSAGE = "Полученный код: ";
    private static final String NUMBER_ERROR = "Ошибка получения кода. Попробуйте повторить позже.";

    private static final String NUMBER_OPERATION = "number";
    private static final String START_OPERATION = "/start";

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final TelegramCodeService telegramCodeService;

    public Bot(TelegramCodeService telegramCodeService) {
        this.telegramCodeService = telegramCodeService;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage response = new SendMessage();
            Long chatId = message.getChatId();
            response.setChatId(String.valueOf(chatId));
            String text = message.getText();
            if (text.equals(NUMBER_OPERATION) || text.equals(START_OPERATION)) {
                try {
                    text = NUMBER_MESSAGE + telegramCodeService.createCode(chatId);
                } catch (ErrorGettingCode e) {
                    LOGGER.error(ErrorGettingCode.TOO_MANY_ATTEMPTS);
                    text=NUMBER_ERROR;
                }
            }
            response.setText(text);
            try {
                execute(response);
            } catch (TelegramApiException ex) {
                String eMessage = String.format(ERROR_SEND_MESSAGE, chatId, text);
                LOGGER.error(eMessage);
                throw new TelegramBotServiceException(eMessage, ex);
            }
        }
    }
}
