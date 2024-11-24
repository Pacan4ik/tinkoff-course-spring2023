package edu.java.scrapper.kafka.consumers;

public interface WorkerMessageHandlerService {
    void handle(WorkerMessage workerMessage);
}
