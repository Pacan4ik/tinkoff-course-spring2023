package edu.java.scrapper.kafka.consumers;

import edu.java.scrapper.UpdatesSender;
import edu.java.scrapper.clients.botClient.BotUpdatesRequest;
import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultWorkerMessageHandlerService implements WorkerMessageHandlerService {
    private final UpdatesSender updatesSender;
    private final LinkInfoAdapter linkInfoAdapter;

    @Override
    public void handle(WorkerMessage workerMessage) {
        Collection<Long> subscribedChats = linkInfoAdapter.getSubscribedChats(workerMessage.id());
        BotUpdatesRequest
            botUpdatesRequest =
            new BotUpdatesRequest(workerMessage.id(), workerMessage.url(), workerMessage.message(), subscribedChats);
        updatesSender.send(botUpdatesRequest);
    }
}
