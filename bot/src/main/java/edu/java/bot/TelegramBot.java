package edu.java.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.processors.MessageProcessor;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TelegramBot implements Bot {
    private final com.pengrad.telegrambot.TelegramBot bot;

    private final ApplicationConfig applicationConfig;
    private final MessageProcessor messageProcessor;

    @Autowired TelegramBot(ApplicationConfig applicationConfig, MessageProcessor messageProcessor) {
        this.applicationConfig = applicationConfig;
        this.messageProcessor = messageProcessor;
        this.bot = new com.pengrad.telegrambot.TelegramBot(applicationConfig.telegramToken());
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            try {
                SendMessage sendMessage = messageProcessor.process(update);
                execute(sendMessage);
            } catch (RuntimeException e) {
                log.warn(String.format("The update id:%d was ignored", update.updateId()));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;

    }

    @Override
    public void start() {
        bot.setUpdatesListener(this);
    }

    @Override
    public void close() {
        bot.shutdown();
    }

    private void setCommands() {
        bot.execute(new SetMyCommands(
            messageProcessor.commands().stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new)
        ));
    }

    @PostConstruct
    public void init() {
        setCommands();
        start();
    }
}
