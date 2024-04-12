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
import io.micrometer.core.instrument.Counter;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TelegramBot implements Bot {
    private final com.pengrad.telegrambot.TelegramBot bot;
    private final MessageProcessor messageProcessor;
    private final Counter processedCounter;
    private final Counter failedCounter;

    TelegramBot(
        ApplicationConfig applicationConfig,
        MessageProcessor messageProcessor,
        @Qualifier("messageProcessedCounter")
        Counter processedCounter,
        @Qualifier("messageFailedCounter")
        Counter failedCounter
    ) {
        this.messageProcessor = messageProcessor;
        this.bot = new com.pengrad.telegrambot.TelegramBot(applicationConfig.telegramToken());
        this.processedCounter = processedCounter;
        this.failedCounter = failedCounter;
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
                processedCounter.increment();
            } catch (RuntimeException e) {
                log.warn(
                    String.format("The update id:%d was ignored", update.updateId())
                    + "\n Cause: " + e
                );
                failedCounter.increment();
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
