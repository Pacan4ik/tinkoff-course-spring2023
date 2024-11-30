package ru.andryxx.gitworker.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.kafka.model.ScrapperRequest;
import ru.andryxx.gitworker.services.MessageHandlerService;

@Service
@Slf4j
public class MessageListener {
    private final MessageHandlerService messageHandler;
    private final ObjectMapper objectMapper;

    public MessageListener(MessageHandlerService messageHandler) {
        this.messageHandler = messageHandler;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
    }

    @RetryableTopic(
        attempts = "1",
        autoCreateTopics = "true",
        kafkaTemplate = "dlqKafkaTemplate",
        dltTopicSuffix = "_dlq"
    )
    @KafkaListener(topics = "${app.kafka.consumer-topic}",
                   containerFactory = "containerFactory")
    public void listen(@Payload String data) {
        log.info("message has been received {}", data);
        ScrapperRequest scrapperRequest;
        try {
            scrapperRequest = objectMapper.readValue(data, ScrapperRequest.class);
            log.info(scrapperRequest.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        messageHandler.handle(scrapperRequest);
    }

}
