package edu.java.bot.api.services;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.TelegramBot;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DefaultUpdateHandlerService implements UpdateHandlerService {
    private final TelegramBot bot;
    private static final String DEFAULT_NOTIFICATION_MESSAGE = "Новое обновление по ссылке:\n";

    public DefaultUpdateHandlerService(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void handleUpdate(URI url, String description, List<Long> ids) {
        for (Long id : ids) {
            bot.execute(new SendMessage(id, DEFAULT_NOTIFICATION_MESSAGE + url.toString() + "\n" + description));
        }
    }
}
