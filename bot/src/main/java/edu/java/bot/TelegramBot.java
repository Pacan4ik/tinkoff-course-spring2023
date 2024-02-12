package edu.java.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.Command;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelegramBot implements Bot {

    private final com.pengrad.telegrambot.TelegramBot bot =
        new com.pengrad.telegrambot.TelegramBot("");
    private final UserMessageProcessor messageProcessor = new UserMessageProcessor();

    TelegramBot() {
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
        bot.execute(new SetMyCommands(
            UserMessageProcessor.commandList.stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new)
        ));
    }

    @Override
    public void close() {
        bot.shutdown();
    }
}
