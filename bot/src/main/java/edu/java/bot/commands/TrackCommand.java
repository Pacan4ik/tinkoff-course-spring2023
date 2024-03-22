package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dataSources.SupportedLinkProvider;
import edu.java.bot.scrapperClient.ScrapperClient;
import edu.java.bot.utils.commands.ParamsParser;
import edu.java.bot.utils.url.ParsedUrl;
import edu.java.bot.utils.url.URLSyntaxException;
import edu.java.bot.utils.url.UrlParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class TrackCommand extends AbstractCommand {

    private final UrlParser urlParser;
    private final ParamsParser paramsParser;

    private final ScrapperClient scrapperClient;

    private final SupportedLinkProvider supportedLinkProvider;

    public TrackCommand(
        UrlParser urlParser,
        ParamsParser paramsParser,
        ScrapperClient scrapperClient,
        SupportedLinkProvider supportedLinkProvider
    ) {
        this.urlParser = urlParser;
        this.paramsParser = paramsParser;
        this.scrapperClient = scrapperClient;
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

        ParsedUrl parsedUrl;
        try {
            parsedUrl = getValidUrlFromMessage(text);
        } catch (IllegalArgumentException e) {
            return new SendMessage(id, e.getMessage());
        }

        String responseMessage;
        try {
            scrapperClient.addTrackingLink(id, parsedUrl.toString());
            responseMessage = "Ссылка успешно добавлена!";
        } catch (WebClientResponseException e) {
            responseMessage = e.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)
                ? "Вы уже отслеживаете данную ссылку"
                : "Произошла ошибка. Попробуйте позднее";
        }

        return new SendMessage(id, responseMessage);
    }

    private ParsedUrl getValidUrlFromMessage(String message) {
        String link = paramsParser.getSingleParam(message)
            .orElseThrow(() -> new IllegalArgumentException(
                "Неправильный формат команды.\nИспользуйте: " + usage()
            ));

        ParsedUrl parsedUrl;
        try {
            parsedUrl = urlParser.parse(link);
        } catch (URLSyntaxException e) {
            throw new IllegalArgumentException("Неправильный формат ссылки.");
        }

        if (!supportedLinkProvider.isSupports(parsedUrl)) {
            throw new IllegalArgumentException("Ссылка не поддерживается");
        }
        return parsedUrl;
    }

}
