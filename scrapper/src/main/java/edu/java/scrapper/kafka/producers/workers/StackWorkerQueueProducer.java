package edu.java.scrapper.kafka.producers.workers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service public class StackWorkerQueueProducer implements WorkerQueueProducer {
    private final KafkaTemplate<String, WorkerCheckRequest> kafkaTemplate;

    public StackWorkerQueueProducer(
        @Qualifier("stackWorkerProducerFactory") KafkaTemplate<String, WorkerCheckRequest> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override public void send(WorkerCheckRequest workerCheckRequest) {
        kafkaTemplate.send(kafkaTemplate.getDefaultTopic(), workerCheckRequest);
    }
}
