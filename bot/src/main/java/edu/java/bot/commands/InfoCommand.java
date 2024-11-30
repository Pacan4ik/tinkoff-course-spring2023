package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InfoCommand extends AbstractCommand {

    public static final String ERROR_MESSAGE = "Ошибка";

    @Override
    public String command() {
        return "/info";
    }

    @Override
    public String description() {
        return "Информация";
    }

    @Override
    public SendMessage handle(Update update) {
        SendMessage message = new SendMessage(update.message().chat().id(), getInfo());
        message.disableWebPagePreview(true);
        return message;
    }

    private String getInfo() {
        try {
            return Files.readString(
                Path.of(
                    Objects.requireNonNull(
                            getClass()
                                .getClassLoader()
                                .getResource("info_response.txt"))
                        .toURI()));
        } catch (IOException | URISyntaxException e) {
            log.error("Error reading info_response.txt", e);
            return ERROR_MESSAGE;
        }
    }
}
