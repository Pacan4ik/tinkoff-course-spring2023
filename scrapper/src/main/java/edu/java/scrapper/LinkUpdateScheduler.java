package edu.java.scrapper;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.botClient.BotClient;
import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.dao.ChatRepository;
import edu.java.scrapper.domain.dao.LinkRepository;
import edu.java.scrapper.domain.dto.ChatDto;
import edu.java.scrapper.domain.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdateScheduler {
    private final ApplicationConfig applicationConfig;
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        for (LinkDto linkDto : linkRepository.findAllWhereCheckedAtBefore(OffsetDateTime.now()
            .minus(applicationConfig.scheduler().linkCheckingFrequency()))) {
            OffsetDateTime lastUpdate = linkDto.updatedAt();
            OffsetDateTime newUpdate;
            try {
                newUpdate = resolve(linkDto.url());
            } catch (Exception e) {
                log.error(e.getMessage());
                continue;
            }
            linkRepository.updateCheckedAt(linkDto.id(), OffsetDateTime.now());
            if (newUpdate.isAfter(lastUpdate)) {
                linkRepository.find(linkDto.id()).ifPresentOrElse(
                    (dto) -> {
                        try {
                            botClient.sendUpdates(
                                dto.id(),
                                dto.url(),
                                "Новое обновление",
                                chatRepository.getAllChats(dto.id()).stream().map(ChatDto::id).toList()
                            );
                            linkRepository.updateUpdatedAt(dto.id(), newUpdate);
                        } catch (ResourceNotFoundException ignored) {
                        } catch (WebClientRequestException | WebClientResponseException e) {
                            log.error("Error during sending updates to the bot");
                            log.error(e.getMessage());
                        }
                    },
                    () -> log.warn(String.format("Link: %s was deleted during update", linkDto))
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
