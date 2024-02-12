package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class StartCommand extends AbstractCommand {
    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Начало работы с ботом";
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        //todo регистрация пользователя (БД)

        return new SendMessage(
            id,
            "Добро пожаловать! Введите /help для просмотра списка команд"
        );
    }
}
