package ru.andryxx.gitworker.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.kafka.model.WorkerMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueProducer {
    public static final String SENDING_WORKER_MESSAGE = "Sending WorkerMessage {}";
    private final KafkaTemplate<String, WorkerMessage> kafkaTemplate;

    public void send(WorkerMessage workerMessage) {
        log.info(SENDING_WORKER_MESSAGE, workerMessage);
        kafkaTemplate.send(kafkaTemplate.getDefaultTopic(), workerMessage);
    }
}
