package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dataSources.UsersTracksDB;
import edu.java.bot.utils.commands.ParamsParser;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component public class UntrackCommand extends AbstractCommand {
    private ParamsParser paramsParser;

    private UsersTracksDB usersTracksDB;

    @Autowired public UntrackCommand(ParamsParser paramsParser, UsersTracksDB usersTracksDB) {
        this.paramsParser = paramsParser;
        this.usersTracksDB = usersTracksDB;
    }

    @Override public String command() {
        return "/untrack";
    }

    @Override public String usage() {
        return command() + " {URL}";
    }

    @Override public String description() {
        return "Прекратить отслеживание ссылки";
    }

    @Override public SendMessage handle(Update update) {
        String text = update.message().text();
        long id = update.message().chat().id();

        Optional<String> oplink = paramsParser.getSingleParam(text);
        if (oplink.isEmpty()) {
            return new SendMessage(id, String.format("Неправильный формат команды.\nИспользуйте: %s", usage()));
        }

        String link = oplink.get();

        if (usersTracksDB.deleteLink(id, link)) {
            return new SendMessage(id, "Ссылка успешно удалена");
        }

        return new SendMessage(id, "Что-то пошло не так. Убедитесь в правильности ссылки.");
    }
}
