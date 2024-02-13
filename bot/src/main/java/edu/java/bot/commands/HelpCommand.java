package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends AbstractCommand {

    private final CommandRegister commandRegister;

    @Autowired HelpCommand(CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
    }

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
        return commandRegister.commandList()
            .stream()
            .map(Objects::toString)
            .collect(Collectors.joining("\n"));
    }
}
