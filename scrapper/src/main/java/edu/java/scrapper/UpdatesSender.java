package edu.java.scrapper;

import edu.java.scrapper.clients.botClient.BotClient;
import edu.java.scrapper.clients.botClient.BotUpdatesRequest;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.kafka.producers.ScrapperQueueProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatesSender {
    private final ApplicationConfig applicationConfig;
    private final BotClient botClient;
    private final ScrapperQueueProducer scrapperQueueProducer;

    public void send(BotUpdatesRequest botUpdatesRequest) {
        switch (applicationConfig.sendingMethod()) {
            case QUEUE -> {
                log.info("Sending via queue");
                scrapperQueueProducer.send(botUpdatesRequest);
            }
            case HTTP -> {
                log.info("Sending via http");
                botClient.sendUpdates(botUpdatesRequest);
            }
            default -> throw new RuntimeException("Unresolved sending method: " + applicationConfig.sendingMethod());
        }
    }
}
