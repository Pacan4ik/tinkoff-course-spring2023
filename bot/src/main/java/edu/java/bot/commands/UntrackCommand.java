package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperClient.ScrapperClient;
import edu.java.bot.utils.commands.ParamsParser;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

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
            var status = scrapperClient.deleteLink(id, link).getStatusCode();
            message = switch (status) {
                case HttpStatusCode code when code.is2xxSuccessful() -> "Ссылка успешно удалена";
                case HttpStatusCode code when code.isSameCodeAs(HttpStatus.NOT_FOUND) ->
                    "Вы не отслеживаете данную ссылку";
                default -> throw new IllegalStateException("Unexpected status: " + status);
            };
        } catch (Exception e) {
            message = "Что-то пошло не так. Попробуйте позднее.";
        }
        return new SendMessage(id, message);
    }
}
