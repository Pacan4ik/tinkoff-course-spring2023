package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import java.util.List;

public class UserMessageProcessor {

    public static List<Command> commandList = List.of(
        new StartCommand(),
        new ListCommand(),
        new TrackCommand(),
        new UntrackCommand(),
        new HelpCommand()
    );

    List<? extends Command> commands() {
        return commandList;
    }

    SendMessage process(Update update) {
        String text = update.message().text().split(" ")[0];
        for (Command command : commandList) {
            if (text.equals(command.command())) {
                return command.handle(update);
            }
        }
        return new SendMessage(
            update.message().chat().id(),
            "Неизвестная команда. Введите /help для просмотра списка команд"
        );
    }
}
