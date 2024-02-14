package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends AbstractCommand implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Просмотр списка команд";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), getCommands());
    }

    private String getCommands() {
        return applicationContext.getBean(CommandRegister.class).commandList()
            .stream()
            .map(Objects::toString)
            .collect(Collectors.joining("\n"));
    }

    @Override
    public void setApplicationContext(final @NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
