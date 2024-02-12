package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UntrackCommand extends AbstractCommand {
    Pattern pattern = Pattern.compile("^/track (\\S+)$");

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        String text = update.message().text();
        long id = update.message().chat().id();
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) {
            return new SendMessage(id, "Введите ссылку");
        }

        String url = matcher.group(1);
        //todo Удалить ссылку из отслеживаемых

        return new SendMessage(id, "Ссылка успешно удалена");
    }
}
