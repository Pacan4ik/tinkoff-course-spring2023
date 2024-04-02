package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperClient.ScrapperClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartCommand extends AbstractCommand {
    private final ScrapperClient scrapperClient;

    public StartCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

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
        String message;
        try {
            scrapperClient.registerChat(id);
            message = "Добро пожаловать! Введите /help для просмотра списка команд.";
        } catch (Exception e) {
            message = "Регистрация на данный момент недоступна";
        }

        return new SendMessage(id, message);
    }
}
