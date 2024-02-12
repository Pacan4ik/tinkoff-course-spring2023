package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Collections;
import java.util.List;

public class ListCommand extends AbstractCommand {
    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();

        //todo получить ссылки
        List<String> urls = Collections.emptyList();

        return new SendMessage(id, "Ваши ссылки:\n" + String.join("\n", urls));
    }
}
