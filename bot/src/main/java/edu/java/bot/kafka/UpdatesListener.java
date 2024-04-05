package edu.java.bot.kafka;

import edu.java.bot.api.model.LinkUpdateRequest;
import edu.java.bot.api.services.UpdateHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdatesListener {
    private final UpdateHandlerService updateHandlerService;

    @KafkaListener(topics = "${app.kafka.consumer-topic}",
                   containerFactory = "containerFactory")
    public void listen(@Payload LinkUpdateRequest linkUpdateRequest) {
        log.info("message has been received {}", linkUpdateRequest.toString());

        updateHandlerService.handleUpdate(
            linkUpdateRequest.url(),
            linkUpdateRequest.description(),
            linkUpdateRequest.tgChatIds()
        );
    }
}
