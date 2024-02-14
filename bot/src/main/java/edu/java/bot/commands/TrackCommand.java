package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dataSources.UsersTracksDB;
import edu.java.bot.utils.commands.ParamsParser;
import edu.java.bot.utils.url.ParsedUrl;
import edu.java.bot.utils.url.URLSyntaxException;
import edu.java.bot.utils.url.UrlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand extends AbstractCommand {

    private UrlParser urlParser;
    private ParamsParser paramsParser;

    private UsersTracksDB usersTracksDB;

    @Autowired
    private void setUrlParser(UrlParser urlParser) {
        this.urlParser = urlParser;
    }

    @Autowired
    private void setParamsParser(ParamsParser paramsParser) {
        this.paramsParser = paramsParser;
    }

    @Autowired
    private void setUsersTracksDB(UsersTracksDB usersTracksDB) {
        this.usersTracksDB = usersTracksDB;
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String usage() {
        return command() + " {URL}";
    }

    @Override
    public String description() {
        return "Начать отслеживать ссылку";
    }

    @Override
    public SendMessage handle(Update update) {
        String text = update.message().text();
        long id = update.message().chat().id();

        String link = paramsParser.getSingleParam(text);
        if (link == null) {
            return new SendMessage(
                id,
                String.format("Неправильный формат команды.\nИспользуйте: %s", usage())
            );
        }

        ParsedUrl parsedUrl = null;
        try {
            parsedUrl = urlParser.parse(link);
        } catch (URLSyntaxException e) {
            return new SendMessage(id, "Неправильный формат ссылки.");
        }

        //todo проверить список поддерживаемых ссылок

        if (usersTracksDB.addLink(id, link)) {
            return new SendMessage(id, "Сслыка успешно добавлена!");
        }

        return new SendMessage(id, "Что-то пошло не так");
    }

}
