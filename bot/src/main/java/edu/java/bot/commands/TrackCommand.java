package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dataSources.SupportedLinkProvider;
import edu.java.bot.scrapperClient.ScrapperClient;
import edu.java.bot.utils.commands.ParamsParser;
import edu.java.bot.utils.url.ParsedUrl;
import edu.java.bot.utils.url.URLSyntaxException;
import edu.java.bot.utils.url.UrlParser;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

        Optional<String> oplink = paramsParser.getSingleParam(text);
        String responseMessage;
        if (oplink.isEmpty()) {
            responseMessage = String.format("Неправильный формат команды.\nИспользуйте: %s", usage());
        } else {
            String link = oplink.get();
            ParsedUrl parsedUrl = null;
            try {
                parsedUrl = urlParser.parse(link);
                if (supportedLinkProvider.isSupported(parsedUrl)) {
                    scrapperClient.addTrackingLink(id, parsedUrl.toString());
                    responseMessage = "Ссылка успешно добавлена!";
                } else {
                    responseMessage = "Ссылка не поддерживается";
                }
            } catch (URLSyntaxException e) {
                responseMessage = "Неправильный формат ссылки.";
            } catch (Exception e) {
                responseMessage = "Не удалось добавить ссылку. Убедитесь, что Вы её уже не отслеживаете.";
            }
        }
        return new SendMessage(id, responseMessage);
    }

}
