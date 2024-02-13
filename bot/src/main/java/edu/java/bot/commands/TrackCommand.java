package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand extends AbstractCommand {

    private final Pattern pattern = Pattern.compile("^/track (https?://)(\\S+)");

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Начать отслеживать ссылку";
    }

    @Override
    public SendMessage handle(Update update) {
        String text = update.message().text();
        long id = update.message().chat().id();
        Matcher matcher = pattern.matcher(text);

        if (!matcher.matches()) {
            return new SendMessage(id, "Невалидная ссылка");
        }

        //todo добавить ссылку в отслеживаемые

        return new SendMessage(id, "Сслыка успешно добавлена");
    }
}
