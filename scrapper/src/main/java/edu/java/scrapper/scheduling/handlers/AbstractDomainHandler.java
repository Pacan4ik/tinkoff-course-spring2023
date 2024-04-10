package edu.java.scrapper.scheduling.handlers;

import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.botClient.BotClient;
import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
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
    protected LinkInfoAdapter linkInfoAdapter;

    public AbstractDomainHandler(BotClient botClient, LinkInfoAdapter linkInfoAdapter) {
        this.botClient = botClient;
        this.linkInfoAdapter = linkInfoAdapter;
    }

    protected abstract boolean isSuitableHost(URI url);

    protected abstract AdditionalHandlerResult getResult(LinkInfoDto linkDto);

    public final void handle(LinkInfoDto linkDto) {
        if (!isSuitableHost(linkDto.getUrl())) {
            handleNext(linkDto);
            return;
        }
        AdditionalHandlerResult result = getResult(linkDto);
        processResult(result, linkDto);
        updateRow(result, linkDto);
    }

    protected final void handleNext(LinkInfoDto linkDto) {
        if (nextSuccessor != null) {
            nextSuccessor.handle(linkDto);
        } else {
            log.warn("No suitable handlers");
        }
    }

    protected void sendUpdate(LinkInfoDto linkDto, String description) {
        try {
            botClient.sendUpdates(
                linkDto.getId(),
                linkDto.getUrl(),
                description,
                linkInfoAdapter.getSubscribedChats(linkDto.getId())
            );
        } catch (ResourceNotFoundException e) {
            log.error(String.format("Link %s not found", linkDto), e); //todo
        } catch (WebClientRequestException | WebClientResponseException e) {
            log.error("Error during sending updates to the bot", e);
        }

    }

    protected void processResult(AdditionalHandlerResult additionalHandlerResult, LinkInfoDto linkDto) {
        List<String> descriptions = additionalHandlerResult.getDescriptions();
        if (!descriptions.isEmpty()) {
            sendUpdate(linkDto, concatDescriptions(descriptions));
        }
    }

    private void updateRow(AdditionalHandlerResult additionalHandlerResult, LinkInfoDto linkDto) {
        LinkInfoDto.AdditionalInfo additionalInfo = linkDto.getAdditionalInfo();
        additionalHandlerResult.getAdditionalInfoConsumer().accept(additionalInfo);
        linkInfoAdapter.updateAdditionalInfo(linkDto.getId(), additionalInfo);

        linkInfoAdapter.updateCheckedAt(linkDto.getId(), OffsetDateTime.now());
    }

    protected String concatDescriptions(List<String> descriptions) {
        return descriptions.size() > 1
            ? "У вас несколько новых обновлений:\n" + String.join(",\n", descriptions)
            : descriptions.getFirst();
    }
}
