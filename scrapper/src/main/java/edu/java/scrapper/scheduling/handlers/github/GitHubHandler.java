package edu.java.scrapper.scheduling.handlers.github;

import edu.java.scrapper.botClient.BotClient;
import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.domain.jdbc.dao.LinkRepository;
import edu.java.scrapper.domain.jdbc.dto.LinkDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AbstractDomainHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public class GitHubHandler extends AbstractDomainHandler {
    private final GitHubClient gitHubClient;
    private static final String HOST = "github.com";
    private final AbstractAdditionalHandler<GitHubResponse> startHandler;

    public GitHubHandler(
        BotClient botClient,
        LinkRepository linkRepository,
        GitHubClient gitHubClient,
        AbstractAdditionalHandler<GitHubResponse> startHandler
    ) {
        super(botClient, linkRepository);
        this.gitHubClient = gitHubClient;
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
            GitHubResponse gitHubResponse = gitHubClient.fetchResponse(path[1], path[2]);
            additionalHandlerResult = startHandler.handle(gitHubResponse, linkDto, additionalHandlerResult);
        } catch (IndexOutOfBoundsException e) {
            log.error("Unable to resolve path: " + linkDto.url(), e);
        } catch (WebClientResponseException e) {
            log.error("Error during fetching response from github api", e);
        }
        return additionalHandlerResult;
    }

}
