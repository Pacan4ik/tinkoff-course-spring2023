package edu.java.scrapper.scheduling.handlers;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.clients.botClient.BotClient;
import edu.java.scrapper.domain.jdbc.dao.ChatRepository;
import edu.java.scrapper.domain.jdbc.dao.LinkRepository;
import edu.java.scrapper.domain.jdbc.dto.ChatDto;
import edu.java.scrapper.domain.jdbc.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public abstract class AbstractDomainHandler {
    @Setter
    protected AbstractDomainHandler nextSuccessor;
    protected BotClient botClient;
    protected LinkRepository linkRepository;
    protected ChatRepository chatRepository;

    public AbstractDomainHandler(BotClient botClient, LinkRepository linkRepository, ChatRepository chatRepository) {
        this.botClient = botClient;
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }

    protected abstract boolean isSuitableHost(URI url);

    protected abstract AdditionalHandlerResult getResult(LinkDto linkDto);

    public final void handle(LinkDto linkDto) {
        if (!isSuitableHost(linkDto.url())) {
            handleNext(linkDto);
            return;
        }
        AdditionalHandlerResult result = getResult(linkDto);
        processResult(result, linkDto);
        updateRow(result, linkDto);
    }

    protected final void handleNext(LinkDto linkDto) {
        if (nextSuccessor != null) {
            nextSuccessor.handle(linkDto);
        } else {
            log.warn("No suitable handlers");
        }
    }

    protected void sendUpdate(LinkDto linkDto, String description) {
        try {
            botClient.sendUpdates(
                linkDto.id(),
                linkDto.url(),
                description,
                chatRepository.getAllChats(linkDto.id()).stream().map(ChatDto::id).toList()
            );
        } catch (ResourceNotFoundException e) {
            log.error(String.format("Link %s not found", linkDto), e);
        } catch (WebClientRequestException | WebClientResponseException e) {
            log.error("Error during sending updates to the bot", e);
        }

    }

    protected void processResult(AdditionalHandlerResult additionalHandlerResult, LinkDto linkDto) {
        List<String> descriptions = additionalHandlerResult.getDescriptions();
        if (!descriptions.isEmpty()) {
            sendUpdate(linkDto, concatDescriptions(descriptions));
        }
    }

    private void updateRow(AdditionalHandlerResult additionalHandlerResult, LinkDto linkDto) {
        additionalHandlerResult.getRowUpdateConsumer().andThen(
            (linkRepository) -> linkRepository.updateCheckedAt(linkDto.id(), OffsetDateTime.now())
        ).accept(linkRepository);
    }

    protected String concatDescriptions(List<String> descriptions) {
        return descriptions.size() > 1
            ? "У вас несколько новых обновлений:\n" + String.join(",\n", descriptions)
            : descriptions.getFirst();
    }
}
