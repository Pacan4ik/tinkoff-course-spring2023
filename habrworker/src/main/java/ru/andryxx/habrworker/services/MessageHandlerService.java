package ru.andryxx.habrworker.services;

import ru.andryxx.habrworker.kafka.model.ScrapperRequest;

public interface MessageHandlerService {
    void handle(ScrapperRequest request);
}
