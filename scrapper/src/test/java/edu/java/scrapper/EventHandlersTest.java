package edu.java.scrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.configuration.EventsHandlersConfig;
import edu.java.scrapper.domain.jdbc.dto.LinkDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EventHandlersTest {

    EventsHandlersConfig eventsHandlersConfig =
        new EventsHandlersConfig(null, null, null, null);

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldProcessTickets() throws JsonProcessingException {
        //given
        AbstractAdditionalHandler<GitHubResponse> gitHubAdditional = eventsHandlersConfig.createGitHubChain();
        GitHubResponse gitHubResponse = new GitHubResponse(
            OffsetDateTime.parse("2024-03-11T00:00:00.0+00:00"),
            OffsetDateTime.parse("2024-03-08T00:00:00.0+00:00"),
            null,
            null,
            3L
        );
        LinkDto linkDto = new LinkDto(
            1L,
            URI.create("https://gtihub.com"),
            OffsetDateTime.parse("2024-03-08T00:00:00.0+00:00"),
            null,
            null,
            objectMapper.readTree("{\"pushed_at\": \"2024-03-11T00:00:00Z\", \"open_issues_count\": 2}")
        );

        //when
        AdditionalHandlerResult result =
            gitHubAdditional.handle(gitHubResponse, linkDto, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый тикет"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessPushes() throws JsonProcessingException {
        //given
        AbstractAdditionalHandler<GitHubResponse> gitHubAdditional = eventsHandlersConfig.createGitHubChain();
        GitHubResponse gitHubResponse = new GitHubResponse(
            OffsetDateTime.parse("2024-03-12T00:00:00.0+00:00"),
            OffsetDateTime.parse("2024-03-08T00:00:00.0+00:00"),
            null,
            null,
            2L
        );
        LinkDto linkDto = new LinkDto(
            1L,
            URI.create("https://gtihub.com"),
            OffsetDateTime.parse("2024-03-08T00:00:00.0+00:00"),
            null,
            null,
            objectMapper.readTree("{\"pushed_at\": \"2024-03-11T00:00:00Z\", \"open_issues_count\": 2}")
        );

        //when
        AdditionalHandlerResult result =
            gitHubAdditional.handle(gitHubResponse, linkDto, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый коммит"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessUnknownChangesGit() throws JsonProcessingException {
        //given
        AbstractAdditionalHandler<GitHubResponse> gitHubAdditional = eventsHandlersConfig.createGitHubChain();
        GitHubResponse gitHubResponse = new GitHubResponse(
            OffsetDateTime.parse("2024-03-11T00:00:00.0+00:00"),
            OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00"),
            null,
            null,
            2L
        );
        LinkDto linkDto = new LinkDto(
            1L,
            URI.create("https://gtihub.com"),
            OffsetDateTime.parse("2024-03-08T00:00:00.0+00:00"),
            null,
            null,
            objectMapper.readTree("{\"pushed_at\": \"2024-03-11T00:00:00Z\", \"open_issues_count\": 2}")
        );

        //when
        AdditionalHandlerResult result =
            gitHubAdditional.handle(gitHubResponse, linkDto, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Новое обновление"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessSeveralChangesGit() throws JsonProcessingException {
        //given
        AbstractAdditionalHandler<GitHubResponse> gitHubAdditional = eventsHandlersConfig.createGitHubChain();
        GitHubResponse gitHubResponse = new GitHubResponse(
            OffsetDateTime.parse("2024-03-12T00:00:00.0+00:00"),
            OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00"),
            null,
            null,
            3L
        );
        LinkDto linkDto = new LinkDto(
            1L,
            URI.create("https://gtihub.com"),
            OffsetDateTime.parse("2024-03-08T00:00:00.0+00:00"),
            null,
            null,
            objectMapper.readTree("{\"pushed_at\": \"2024-03-11T00:00:00Z\", \"open_issues_count\": 2}")
        );

        //when
        AdditionalHandlerResult result =
            gitHubAdditional.handle(gitHubResponse, linkDto, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый тикет", "Появился новый коммит"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessAnswers() throws JsonProcessingException {
        //given
        AbstractAdditionalHandler<StackOverFlowResponse> stackOverflowAdditional =
            eventsHandlersConfig.createStackOverflowChain();
        StackOverFlowResponse stackOverFlowResponse = new StackOverFlowResponse(
            List.of(
                new StackOverFlowResponse.Item(
                    OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00"),
                    null,
                    null,
                    null,
                    2L,
                    3L
                )
            )
        );
        LinkDto linkDto = new LinkDto(
            1L,
            URI.create("https://stackoverflow.com"),
            OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00"),
            null,
            null,
            objectMapper.readTree("{\"comment_count\": 2, \"answer_count\": 2}")
        );

        //when
        AdditionalHandlerResult result =
            stackOverflowAdditional.handle(stackOverFlowResponse, linkDto, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый ответ"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessComments() throws JsonProcessingException {
        //given
        AbstractAdditionalHandler<StackOverFlowResponse> stackOverflowAdditional =
            eventsHandlersConfig.createStackOverflowChain();
        StackOverFlowResponse stackOverFlowResponse = new StackOverFlowResponse(
            List.of(
                new StackOverFlowResponse.Item(
                    OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00"),
                    null,
                    null,
                    null,
                    3L,
                    2L
                )
            )
        );
        LinkDto linkDto = new LinkDto(
            1L,
            URI.create("https://stackoverflow.com"),
            OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00"),
            null,
            null,
            objectMapper.readTree("{\"comment_count\": 2, \"answer_count\": 2}")
        );

        //when
        AdditionalHandlerResult result =
            stackOverflowAdditional.handle(stackOverFlowResponse, linkDto, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый комментарий"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessUnknownChangesStack() throws JsonProcessingException {
        //given
        AbstractAdditionalHandler<StackOverFlowResponse> stackOverflowAdditional =
            eventsHandlersConfig.createStackOverflowChain();
        StackOverFlowResponse stackOverFlowResponse = new StackOverFlowResponse(
            List.of(
                new StackOverFlowResponse.Item(
                    OffsetDateTime.parse("2024-03-11T00:00:00.0+00:00"),
                    null,
                    null,
                    null,
                    2L,
                    2L
                )
            )
        );
        LinkDto linkDto = new LinkDto(
            1L,
            URI.create("https://stackoverflow.com"),
            OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00"),
            null,
            null,
            objectMapper.readTree("{\"comment_count\": 2, \"answer_count\": 2}")
        );

        //when
        AdditionalHandlerResult result =
            stackOverflowAdditional.handle(stackOverFlowResponse, linkDto, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Новое обновление"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessSeveralChangesStack() throws JsonProcessingException {
        //given
        AbstractAdditionalHandler<StackOverFlowResponse> stackOverflowAdditional =
            eventsHandlersConfig.createStackOverflowChain();
        StackOverFlowResponse stackOverFlowResponse = new StackOverFlowResponse(
            List.of(
                new StackOverFlowResponse.Item(
                    OffsetDateTime.parse("2024-03-11T00:00:00.0+00:00"),
                    null,
                    null,
                    null,
                    3L,
                    3L
                )
            )
        );
        LinkDto linkDto = new LinkDto(
            1L,
            URI.create("https://stackoverflow.com"),
            OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00"),
            null,
            null,
            objectMapper.readTree("{\"comment_count\": 2, \"answer_count\": 2}")
        );

        //when
        AdditionalHandlerResult result =
            stackOverflowAdditional.handle(stackOverFlowResponse, linkDto, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый ответ", "Появился новый комментарий"),
            result.getDescriptions()
        );
    }

}
