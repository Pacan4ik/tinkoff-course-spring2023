package edu.java.scrapper;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.repositories.LinkRepository;
import edu.java.scrapper.botClient.BotClient;
import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Log4j2
@Component
public class LinkUpdateScheduler {
    private final ApplicationConfig applicationConfig;
    private final LinkRepository linkRepository;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    public LinkUpdateScheduler(
        ApplicationConfig applicationConfig,
        LinkRepository linkRepository,
        GitHubClient gitHubClient,
        StackOverflowClient stackOverflowClient,
        BotClient botClient
    ) {
        this.applicationConfig = applicationConfig;
        this.linkRepository = linkRepository;

        this.gitHubClient = gitHubClient;
        this.stackOverflowClient = stackOverflowClient;
        this.botClient = botClient;
    }

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        for (URI url : linkRepository.getLinksCheckedBefore(OffsetDateTime.now()
            .minus(applicationConfig.scheduler().linkCheckingFrequency()))) {
            OffsetDateTime lastUpdate = linkRepository.getLastUpdate(url);
            OffsetDateTime newUpdate;
            try {
                newUpdate = resolve(url);
            } catch (Exception e) {
                log.error(e.getMessage());
                continue;
            }
            linkRepository.updateCheckedAt(url, OffsetDateTime.now());
            if (newUpdate.isAfter(lastUpdate)) {
                linkRepository.findLink(url).ifPresentOrElse(
                    (id) -> {
                        try {
                            botClient.sendUpdates(
                                id,
                                url,
                                "Новое обновление",
                                linkRepository.getSubscribedChats(url)
                            );
                            linkRepository.updateUpdatedAt(url, newUpdate);
                        } catch (ResourceNotFoundException ignored) {
                        } catch (WebClientRequestException | WebClientResponseException e) {
                            log.error("Error during sending updates to the bot");
                            log.error(e.getMessage());
                        }
                    },
                    () -> log.warn(String.format("URL: %s was deleted during update", url))
                );
            }
        }
    }

    private OffsetDateTime resolve(URI url) {
        String[] path;
        OffsetDateTime newUpdate;
        switch (url.getHost()) {
            case "github.com":
                path = url.getPath().split("/");
                GitHubResponse gitHubResponse = gitHubClient.fetchResponse(path[1], path[2]);
                newUpdate = gitHubResponse.pushedAt();
                break;
            case "stackoverflow.com":
                path = url.getPath().split("/");
                StackOverFlowResponse stackOverFlowResponse = stackOverflowClient.fetchResponse(path[2]);
                newUpdate = stackOverFlowResponse.items().getFirst().lastActivityDate();
                break;
            default:
                throw new RuntimeException("Unsupported host in: " + url);
        }
        return newUpdate;
    }

}
