package edu.java.bot.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandRegister;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserMessageProcessor implements MessageProcessor {

    private final CommandRegister commandRegister;

    public UserMessageProcessor(CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
    }

    public List<? extends Command> commands() {
        return commandRegister.commandList();
    }

    public SendMessage process(Update update) {
        String text = update.message().text().split(" ")[0];
        for (Command command : commandRegister.commandList()) {
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
