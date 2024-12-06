package edu.java.scrapper.kafka.producers.workers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service public class GitWorkerQueueProducer implements WorkerQueueProducer {
    private final KafkaTemplate<String, WorkerCheckRequest> kafkaTemplate;

    public GitWorkerQueueProducer(
        @Qualifier("gitWorkerKafkaTemplate") KafkaTemplate<String, WorkerCheckRequest> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(WorkerCheckRequest workerCheckRequest) {
        kafkaTemplate.send(kafkaTemplate.getDefaultTopic(), workerCheckRequest);
    }
}
