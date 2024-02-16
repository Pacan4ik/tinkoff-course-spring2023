import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.AbstractCommand;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandRegister;
import edu.java.bot.processors.MessageProcessor;
import edu.java.bot.processors.UserMessageProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageProcessorTest {
    @Test
    void shouldChooseCommand() {
        //prepare
        Update update = mockPrepare(123L, "/test2");
        Map<String, Object> expectedParameters = new HashMap<>();
        expectedParameters.put("chat_id", 123L);
        expectedParameters.put("text", "test2");

        //given
        MessageProcessor messageProcessor = new UserMessageProcessor(prepareCommandRegister());

        //when
        SendMessage sendMessage  = messageProcessor.process(update);

        //then
        Assertions.assertEquals(expectedParameters, sendMessage.getParameters());
    }

    @Test
    void shouldInformIfUnknownCommand() {
        //prepare
        Update update = mockPrepare(123L, "/unknownCom");
        Map<String, Object> expectedParameters = new HashMap<>();
        expectedParameters.put("chat_id", 123L);
        expectedParameters.put("text", "Неизвестная команда. Введите /help для просмотра списка команд");

        //given
        MessageProcessor messageProcessor = new UserMessageProcessor(prepareCommandRegister());

        //when
        SendMessage sendMessage  = messageProcessor.process(update);

        //then
        Assertions.assertEquals(expectedParameters, sendMessage.getParameters());
    }

    Update mockPrepare(long id, String messageText) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(chat.id()).thenReturn(id);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn(messageText);
        when(update.message()).thenReturn(message);

        return update;
    }

    static CommandRegister prepareCommandRegister() {
        Command testCommand1 = new AbstractCommand() {
            @Override
            public String command() {
                return "/test1";
            }

            @Override
            public String description() {
                return "Description of testCommand1";
            }

            @Override
            public SendMessage handle(Update update) {
                return new SendMessage(update.message().chat().id(), "test1");
            }
        };
        Command testCommand2 = new AbstractCommand() {
            @Override
            public String command() {
                return "/test2";
            }

            @Override
            public String description() {
                return "Description of testCommand2";
            }

            @Override
            public SendMessage handle(Update update) {
                return new SendMessage(update.message().chat().id(), "test2");
            }
        };

        return new CommandRegister(List.of(testCommand1, testCommand2));
    }
}
