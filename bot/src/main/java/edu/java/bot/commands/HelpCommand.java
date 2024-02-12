package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.UserMessageProcessor;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HelpCommand extends AbstractCommand {
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
        List<Command> commands = UserMessageProcessor.commandList;
        return commands.stream()
            .map(Objects::toString)
            .collect(Collectors.joining("\n"));
    }
}
