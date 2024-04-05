package edu.java.scrapper.kafka;

import edu.java.scrapper.clients.botClient.BotUpdatesRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, BotUpdatesRequest> kafkaTemplate;

    public void send(BotUpdatesRequest botUpdatesRequest) {
        kafkaTemplate.send(kafkaTemplate.getDefaultTopic(), botUpdatesRequest);
    }
}
