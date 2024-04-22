package edu.java.scrapper.scheduling.handlers.stackoverflow;

import edu.java.scrapper.clients.botClient.BotClient;
import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
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
        LinkInfoAdapter linkInfoAdapter,
        StackOverflowClient stackOverflowClient,
        AbstractAdditionalHandler<StackOverFlowResponse> startHandler
    ) {
        super(botClient, linkInfoAdapter);
        this.stackOverflowClient = stackOverflowClient;
        this.startHandler = startHandler;
    }

    @Override
    protected boolean isSuitableHost(URI url) {
        return url.getHost().equals(HOST);
    }

    @Override
    protected AdditionalHandlerResult getResult(LinkInfoDto linkDto) {
        String[] path = linkDto.getUrl().getPath().split("/");
        AdditionalHandlerResult additionalHandlerResult = new AdditionalHandlerResult();
        try {
            StackOverFlowResponse stackOverFlowResponse = stackOverflowClient.fetchResponse(path[2]);
            additionalHandlerResult =
                startHandler.handle(stackOverFlowResponse, linkDto.getAdditionalInfo(), additionalHandlerResult);
        } catch (IndexOutOfBoundsException e) {
            log.error("Unable to resolve path: " + linkDto.getUrl(), e);
        } catch (WebClientResponseException e) {
            log.error("Error during fetching response from stackoverflow api", e);
        }
        return additionalHandlerResult;
    }
}
