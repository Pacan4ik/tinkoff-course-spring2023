package edu.java.bot.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.api.model.LinkUpdateRequest;
import edu.java.bot.api.services.UpdateHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdatesListener {
    private final UpdateHandlerService updateHandlerService;
    private final ObjectMapper objectMapper;

    public UpdatesListener(UpdateHandlerService updateHandlerService) {
        this.updateHandlerService = updateHandlerService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
    }

    @RetryableTopic(
        attempts = "1",
        autoCreateTopics = "false",
        kafkaTemplate = "kafkaTemplate",
        dltTopicSuffix = "_dlq"
    )
    @KafkaListener(topics = "${app.kafka.consumer-topic}",
                   containerFactory = "containerFactory")
    public void listen(@Payload String data) {
        log.info("message has been received {}", data);
        LinkUpdateRequest linkUpdateRequest;
        try {
            linkUpdateRequest = objectMapper.readValue(data, LinkUpdateRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        updateHandlerService.handleUpdate(
            linkUpdateRequest.url(),
            linkUpdateRequest.description(),
            linkUpdateRequest.tgChatIds()
        );
    }
}
