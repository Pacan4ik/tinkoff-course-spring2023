package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dataSources.UsersTracksDB;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand extends AbstractCommand {
    private UsersTracksDB usersTracksDB;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Autowired
    private void setUsersTracksDB(UsersTracksDB usersTracksDB) {
        this.usersTracksDB = usersTracksDB;
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();

        List<String> urls = Objects.requireNonNullElse(usersTracksDB.getUserLinks(id), Collections.emptyList());

        return new SendMessage(id, "Ваши ссылки:\n" + String.join("\n", urls));
    }
}
