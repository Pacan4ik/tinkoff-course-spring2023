package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperClient.ScrapperClient;
import edu.java.bot.scrapperClient.model.ListLinksResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
            ResponseEntity<ListLinksResponse> response = scrapperClient.getTrackingLinks(id);
            List<String> numberedUrls = getNumberedUrls(response);
            message = "Ваши ссылки:\n" + String.join("\n", numberedUrls);
        } catch (WebClientResponseException e) {
            message = e.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)
                ? "Вы не зарегистрированы"
                : "Произошла ошибка. Попробуйте позднее";
        }

        return new SendMessage(id, message)
            .disableWebPagePreview(true);
    }

    @NotNull
    private List<String> getNumberedUrls(ResponseEntity<ListLinksResponse> response) {
        List<String> urls = Objects.requireNonNullElseGet(
                response.getBody(),
                () -> new ListLinksResponse(Collections.emptyList(), 0)
            ).links().stream()
            .map((linkResponse) -> linkResponse.url().toString())
            .toList();
        return IntStream.range(0, urls.size())
            .mapToObj(i -> (i + 1) + ". " + urls.get(i))
            .toList();
    }
}
