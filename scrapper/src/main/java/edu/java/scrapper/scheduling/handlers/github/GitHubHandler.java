package edu.java.scrapper.scheduling.handlers.github;

import edu.java.scrapper.UpdatesSender;
import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
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
        UpdatesSender updatesSender,
        LinkInfoAdapter linkInfoAdapter,
        GitHubClient gitHubClient,
        AbstractAdditionalHandler<GitHubResponse> startHandler
    ) {
        super(updatesSender, linkInfoAdapter);
        this.gitHubClient = gitHubClient;
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
            GitHubResponse gitHubResponse = gitHubClient.fetchResponse(path[1], path[2]);
            additionalHandlerResult =
                startHandler.handle(gitHubResponse, linkDto.getAdditionalInfo(), additionalHandlerResult);
        } catch (IndexOutOfBoundsException e) {
            log.error("Unable to resolve path: " + linkDto.getUrl(), e);
        } catch (WebClientResponseException e) {
            log.error("Error during fetching response from github api", e);
        }
        return additionalHandlerResult;
    }

}
