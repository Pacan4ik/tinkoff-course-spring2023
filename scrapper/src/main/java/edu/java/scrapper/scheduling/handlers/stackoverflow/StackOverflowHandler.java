package edu.java.scrapper.scheduling.handlers.stackoverflow;

import edu.java.scrapper.botClient.BotClient;
import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import edu.java.scrapper.domain.jdbc.dao.ChatRepository;
import edu.java.scrapper.domain.jdbc.dao.LinkRepository;
import edu.java.scrapper.domain.jdbc.dto.LinkDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AbstractDomainHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class StackOverflowHandler extends AbstractDomainHandler {
    private final StackOverflowClient stackOverflowClient;
    private static final String HOST = "stackoverflow.com";
    private final AbstractAdditionalHandler<StackOverFlowResponse> startHandler;

    public StackOverflowHandler(
        BotClient botClient,
        LinkRepository linkRepository,
        ChatRepository chatRepository,
        StackOverflowClient stackOverflowClient,
        AbstractAdditionalHandler<StackOverFlowResponse> startHandler
    ) {
        super(botClient, linkRepository, chatRepository);
        this.stackOverflowClient = stackOverflowClient;
        this.startHandler = startHandler;
    }

    @Override
    protected boolean isSuitableHost(URI url) {
        return url.getHost().equals(HOST);
    }

    @Override
    protected AdditionalHandlerResult getResult(LinkDto linkDto) {
        String[] path = linkDto.url().getPath().split("/");
        AdditionalHandlerResult additionalHandlerResult = new AdditionalHandlerResult();
        try {
            StackOverFlowResponse stackOverFlowResponse = stackOverflowClient.fetchResponse(path[2]);
            additionalHandlerResult = startHandler.handle(stackOverFlowResponse, linkDto, additionalHandlerResult);
        } catch (IndexOutOfBoundsException e) {
            log.error("Unable to resolve path: " + linkDto.url(), e);
        } catch (WebClientResponseException e) {
            log.error("Error during fetching response from stackoverflow api", e);
        }
        return additionalHandlerResult;
    }
}
