package edu.java.scrapper;

import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.configuration.EventsHandlersConfig;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventHandlersTest {

    EventsHandlersConfig eventsHandlersConfig =
        new EventsHandlersConfig(null, null, null, null);

    private static final LinkInfoDto.AdditionalInfo DEFAULT_INFO = new LinkInfoDto.AdditionalInfo(
        2L,
        OffsetDateTime.parse("2024-03-11T00:00:00Z"),
        OffsetDateTime.parse("2024-03-08T00:00:00.0+00:00"),
        2L,
        2L,
        OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00")
    );

    @Test
    void shouldProcessTickets() {
        //given
        AbstractAdditionalHandler<GitHubResponse> gitHubAdditional = eventsHandlersConfig.createGitHubChain();
        GitHubResponse gitHubResponse = new GitHubResponse(
            OffsetDateTime.parse("2024-03-11T00:00:00.0+00:00"),
            OffsetDateTime.parse("2024-03-08T00:00:00.0+00:00"),
            null,
            null,
            3L
        );

        //when
        AdditionalHandlerResult result =
            gitHubAdditional.handle(gitHubResponse, DEFAULT_INFO, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый тикет"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessPushes() {
        //given
        AbstractAdditionalHandler<GitHubResponse> gitHubAdditional = eventsHandlersConfig.createGitHubChain();
        GitHubResponse gitHubResponse = new GitHubResponse(
            OffsetDateTime.parse("2024-03-12T00:00:00.0+00:00"),
            OffsetDateTime.parse("2024-03-08T00:00:00.0+00:00"),
            null,
            null,
            2L
        );

        //when
        AdditionalHandlerResult result =
            gitHubAdditional.handle(gitHubResponse, DEFAULT_INFO, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый коммит"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessUnknownChangesGit() {
        //given
        AbstractAdditionalHandler<GitHubResponse> gitHubAdditional = eventsHandlersConfig.createGitHubChain();
        GitHubResponse gitHubResponse = new GitHubResponse(
            OffsetDateTime.parse("2024-03-11T00:00:00.0+00:00"),
            OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00"),
            null,
            null,
            2L
        );

        //when
        AdditionalHandlerResult result =
            gitHubAdditional.handle(gitHubResponse, DEFAULT_INFO, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Новое обновление"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessSeveralChangesGit() {
        //given
        AbstractAdditionalHandler<GitHubResponse> gitHubAdditional = eventsHandlersConfig.createGitHubChain();
        GitHubResponse gitHubResponse = new GitHubResponse(
            OffsetDateTime.parse("2024-03-12T00:00:00.0+00:00"),
            OffsetDateTime.parse("2024-03-09T00:00:00.0+00:00"),
            null,
            null,
            3L
        );

        //when
        AdditionalHandlerResult result =
            gitHubAdditional.handle(gitHubResponse, DEFAULT_INFO, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый тикет", "Появился новый коммит"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessAnswers() {
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

        //when
        AdditionalHandlerResult result =
            stackOverflowAdditional.handle(stackOverFlowResponse, DEFAULT_INFO, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый ответ"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessComments() {
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

        //when
        AdditionalHandlerResult result =
            stackOverflowAdditional.handle(stackOverFlowResponse, DEFAULT_INFO, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый комментарий"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessUnknownChangesStack() {
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

        //when
        AdditionalHandlerResult result =
            stackOverflowAdditional.handle(stackOverFlowResponse, DEFAULT_INFO, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Новое обновление"),
            result.getDescriptions()
        );
    }

    @Test
    void shouldProcessSeveralChangesStack() {
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

        //when
        AdditionalHandlerResult result =
            stackOverflowAdditional.handle(stackOverFlowResponse, DEFAULT_INFO, new AdditionalHandlerResult());

        //then
        Assertions.assertEquals(
            List.of("Появился новый ответ", "Появился новый комментарий"),
            result.getDescriptions()
        );
    }

}
