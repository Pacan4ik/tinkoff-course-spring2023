package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperClient.ScrapperClient;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ListCommand extends AbstractCommand {
    private final ScrapperClient scrapperClient;

    public ListCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        String message;
        try {
            var urls = Objects.requireNonNull(scrapperClient.getTrackingLinks(id).getBody()).links().stream()
                .map((linkResponse) -> linkResponse.url().toString())
                .toList();
            var numberedUrls = IntStream.range(0, urls.size())
                .mapToObj(i -> (i + 1) + ". " + urls.get(i))
                .toList();
            message = "Ваши ссылки:\n" + String.join("\n", numberedUrls);
        } catch (Exception e) {
            message = "Произошла ошибка. Убедитесь, что Вы зарегистрированы";
        }

        return new SendMessage(id, message)
            .disableWebPagePreview(true);
    }
}
