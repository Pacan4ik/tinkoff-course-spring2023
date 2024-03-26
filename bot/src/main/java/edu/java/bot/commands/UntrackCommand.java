package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperClient.ScrapperClient;
import edu.java.bot.utils.commands.ParamsParser;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class UntrackCommand extends AbstractCommand {
    private final ParamsParser paramsParser;

    private final ScrapperClient scrapperClient;

    public UntrackCommand(ParamsParser paramsParser, ScrapperClient scrapperClient) {
        this.paramsParser = paramsParser;
        this.scrapperClient = scrapperClient;
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
        String message;
        try {
            scrapperClient.deleteLink(id, link);
            message = "Ссылка успешно удалена";
        } catch (WebClientResponseException e) {
            message = e.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)
                ? "Вы не отслеживаете данную ссылку"
                : "Произошла ошибка. Попробуйте позднее";
        }
        return new SendMessage(id, message);
    }
}
