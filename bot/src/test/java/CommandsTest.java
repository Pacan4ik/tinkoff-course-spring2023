import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.AbstractCommand;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandRegister;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.InfoCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.dataSources.StubLinkProvider;
import edu.java.bot.scrapperClient.ScrapperClient;
import edu.java.bot.scrapperClient.model.LinkResponse;
import edu.java.bot.scrapperClient.model.ListLinksResponse;
import edu.java.bot.utils.commands.ParamsParser;
import edu.java.bot.utils.url.SimpleUrlParser;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandsTest {

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

    static ScrapperClient mockScrapper(Long chatId) {
        ScrapperClient scrapperClient = mock(ScrapperClient.class);

        when(scrapperClient.registerChat(chatId))
            .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        when(scrapperClient.addTrackingLink(chatId, "https://stackoverflow.com/questions/123"))
            .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        when(scrapperClient.getTrackingLinks(chatId))
            .thenReturn(new ResponseEntity<>(
                    new ListLinksResponse(
                        List.of(new LinkResponse(0L, URI.create("https://google.com"))),
                        1
                    ), HttpStatusCode.valueOf(200)
                )
            );
        when(scrapperClient.deleteLink(chatId, "https://google.com"))
            .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        when(scrapperClient.deleteLink(chatId, "https://github.com"))
            .thenThrow(new WebClientResponseException(404, "Not found", null, null, null));
        return scrapperClient;
    }

    static Arguments[] commandsOutput() throws URISyntaxException, IOException {
        return new Arguments[] {
            Arguments.of(
                new StartCommand(mockScrapper(123L)),
                123L,
                "/start",
                "Добро пожаловать! Введите /help для просмотра списка команд.",
                false
            ),
            Arguments.of(
                new TrackCommand(
                    new SimpleUrlParser(),
                    new ParamsParser(),
                    mockScrapper(123L),
                    new StubLinkProvider()
                ),
                123L,
                "/track",
                "Неправильный формат команды.\nИспользуйте: /track {URL}",
                false
            ),
            Arguments.of(
                new TrackCommand(
                    new SimpleUrlParser(),
                    new ParamsParser(),
                    mockScrapper(123L),
                    new StubLinkProvider()
                ),
                123L,
                "/track https://stackoverflow.com/questions/123",
                "Ссылка успешно добавлена!",
                false
            ),
            Arguments.of(
                new TrackCommand(
                    new SimpleUrlParser(),
                    new ParamsParser(),
                    mockScrapper(123L),
                    new StubLinkProvider()
                ),
                123L,
                "/track https://translate.google.com",
                "Ссылка не поддерживается",
                false
            ),
            Arguments.of(
                new TrackCommand(
                    new SimpleUrlParser(),
                    new ParamsParser(),
                    mockScrapper(123L),
                    new StubLinkProvider()
                ),
                123L,
                "/track www.youtube.com",
                "Неправильный формат ссылки.",
                false
            ),
            Arguments.of(
                new ListCommand(mockScrapper(123L)),
                123L,
                "/list",
                "Ваши ссылки:\n1. https://google.com",
                true
            ),
            Arguments.of(
                new UntrackCommand(
                    new ParamsParser(),
                    mockScrapper(123L)
                ),
                123L,
                "/untrack https://google.com",
                "Ссылка успешно удалена",
                false
            ),
            Arguments.of(
                new UntrackCommand(
                    new ParamsParser(),
                    mockScrapper(123L)
                ),
                123L,
                "/untrack",
                "Неправильный формат команды.\nИспользуйте: /untrack {URL}",
                false
            ),
            Arguments.of(
                new UntrackCommand(
                    new ParamsParser(),
                    mockScrapper(123L)
                ),
                123L,
                "/untrack https://github.com",
                "Вы не отслеживаете данную ссылку",
                false
            ),
            Arguments.of(new InfoCommand(), 123L, "/info", Files.readString(Path.of(Objects.requireNonNull(
                    CommandsTest.class
                        .getClassLoader()
                        .getResource("info_response.txt"))
                .toURI())), true),

            Arguments.of(
                prepareHelpCommand(),
                123L,
                "/help",
                "/test1\t - \tDescription of testCommand1\n/test2\t - \tDescription of testCommand2",
                false
            )
        };
    }

    @ParameterizedTest
    @MethodSource("commandsOutput")
    void commandsTest(Command command, long id, String input, String expectedOutput, boolean disableWebPagePreview) {
        //prepare
        Update update = mockPrepare(id, input);

        Map<String, Object> expectedParameters = new HashMap<>();
        expectedParameters.put("chat_id", id);
        expectedParameters.put("text", expectedOutput);
        if (disableWebPagePreview) {
            expectedParameters.put("disable_web_page_preview", true);
        }

        //when
        SendMessage sendMessage = command.handle(update);

        //then
        Assertions.assertEquals(expectedParameters, sendMessage.getParameters());

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
                return null;
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
                return null;
            }
        };

        return new CommandRegister(List.of(testCommand1, testCommand2));
    }

    static HelpCommand prepareHelpCommand() {
        HelpCommand helpCommand = new HelpCommand();
        helpCommand.setCommandRegister(prepareCommandRegister());
        return helpCommand;
    }

}
