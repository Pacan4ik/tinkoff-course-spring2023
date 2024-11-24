package edu.java.scrapper.kafka.producers.workers;

public interface WorkerQueueProducer {
    void send(WorkerCheckRequest workerCheckRequest);
}
