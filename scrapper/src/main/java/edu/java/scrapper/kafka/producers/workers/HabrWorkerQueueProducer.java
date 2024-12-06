package edu.java.scrapper.kafka.producers.workers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class HabrWorkerQueueProducer implements WorkerQueueProducer {
    private final KafkaTemplate<String, WorkerCheckRequest> kafkaTemplate;

    public HabrWorkerQueueProducer(
        @Qualifier("habrWorkerKafkaTemplate")
        KafkaTemplate<String, WorkerCheckRequest> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(WorkerCheckRequest workerCheckRequest) {
        kafkaTemplate.send(kafkaTemplate.getDefaultTopic(), workerCheckRequest);
    }
}
