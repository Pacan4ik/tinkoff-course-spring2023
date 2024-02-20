package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dataSources.SupportedLinkProvider;
import edu.java.bot.dataSources.UsersTracksDB;
import edu.java.bot.utils.commands.ParamsParser;
import edu.java.bot.utils.url.ParsedUrl;
import edu.java.bot.utils.url.URLSyntaxException;
import edu.java.bot.utils.url.UrlParser;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component public class TrackCommand extends AbstractCommand {

    private final UrlParser urlParser;
    private final ParamsParser paramsParser;

    private final UsersTracksDB usersTracksDB;

    private final SupportedLinkProvider supportedLinkProvider;

    public TrackCommand(
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

        Optional<String> oplink = paramsParser.getSingleParam(text);
        String responseMessage;
        if (oplink.isPresent()) {
            String link = oplink.get();
            ParsedUrl parsedUrl = null;
            try {
                parsedUrl = urlParser.parse(link);
                if (supportedLinkProvider.getSupportedLinks().contains(parsedUrl)) {
                    responseMessage =
                        usersTracksDB.addLink(id, link) ? "Ссылка успешно добавлена!" : "Что-то пошло не так";
                } else {
                    responseMessage = "Ссылка не поддерживается";
                }
            } catch (URLSyntaxException e) {
                responseMessage = "Неправильный формат ссылки.";
            }
        } else {
            responseMessage = String.format("Неправильный формат команды.\nИспользуйте: %s", usage());
        }
        return new SendMessage(id, responseMessage);
    }

}
