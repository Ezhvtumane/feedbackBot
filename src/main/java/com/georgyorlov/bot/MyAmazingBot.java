package com.georgyorlov.bot;


import com.georgyorlov.SendToNotion;
import com.georgyorlov.util.PropertiesExtractor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyAmazingBot extends TelegramLongPollingBot {
    /*
     * Если сообщение от админа - то парсим id чата(юзера) и шлем туда сообщение.
     * */
    private final SendToNotion sendToNotion = new SendToNotion();
    private final String botName = PropertiesExtractor.get().getProperty("bot.name");
    private final String botSecret = PropertiesExtractor.get().getProperty("bot.secret");

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String nickName = update.getMessage().getFrom().getUserName();
            long userId = update.getMessage().getFrom().getId();
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(chatId);
            message.setText("Thanks for your message. Administrator will answer you later(or not).");
            try {
                sendToNotion.send(userId, chatId, nickName, text);
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botSecret;
    }
}
