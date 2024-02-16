package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dataSources.SupportedLinkProvider;
import edu.java.bot.dataSources.UsersTracksDB;
import edu.java.bot.utils.commands.ParamsParser;
import edu.java.bot.utils.url.ParsedUrl;
import edu.java.bot.utils.url.URLSyntaxException;
import edu.java.bot.utils.url.UrlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component public class TrackCommand extends AbstractCommand {

    private final UrlParser urlParser;
    private final ParamsParser paramsParser;

    private final UsersTracksDB usersTracksDB;

    private final SupportedLinkProvider supportedLinkProvider;

    @Autowired public TrackCommand(
        UrlParser urlParser,
        ParamsParser paramsParser,
        UsersTracksDB usersTracksDB,
        SupportedLinkProvider supportedLinkProvider
    ) {
        this.urlParser = urlParser;
        this.paramsParser = paramsParser;
        this.usersTracksDB = usersTracksDB;
        this.supportedLinkProvider = supportedLinkProvider;
    }

    @Override public String command() {
        return "/track";
    }

    @Override public String usage() {
        return command() + " {URL}";
    }

    @Override public String description() {
        return "Начать отслеживать ссылку";
    }

    @Override public SendMessage handle(Update update) {
        String text = update.message().text();
        long id = update.message().chat().id();

        String link = paramsParser.getSingleParam(text);
        if (link == null) {
            return new SendMessage(id, String.format("Неправильный формат команды.\nИспользуйте: %s", usage()));
        }

        ParsedUrl parsedUrl = null;
        try {
            parsedUrl = urlParser.parse(link);
        } catch (URLSyntaxException e) {
            return new SendMessage(id, "Неправильный формат ссылки.");
        }

        if (!supportedLinkProvider.getSupportedLinks().contains(parsedUrl)) {
            return new SendMessage(id, "Ссылка не поддерживается");
        }

        if (usersTracksDB.addLink(id, link)) {
            return new SendMessage(id, "Ссылка успешно добавлена!");
        }

        return new SendMessage(id, "Что-то пошло не так");
    }

}
