package edu.java.scrapper.kafka.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageListener {
    private final WorkerMessageHandlerService workerMessageHandlerService;
    private final ObjectMapper objectMapper;

    public MessageListener(WorkerMessageHandlerService workerMessageHandlerService) {
        this.workerMessageHandlerService = workerMessageHandlerService;
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
    @KafkaListener(topics = "${app.kafka.workers-consumer-topic}",
                   containerFactory = "containerFactory")
    public void listen(@Payload String data) {
        log.info("message has been received {}", data);
        WorkerMessage workerMessage;
        try {
            workerMessage = objectMapper.readValue(data, WorkerMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        workerMessageHandlerService.handle(workerMessage);
    }

}
