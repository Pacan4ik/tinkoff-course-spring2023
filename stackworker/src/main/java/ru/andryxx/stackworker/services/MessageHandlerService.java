package ru.andryxx.stackworker.services;

import ru.andryxx.stackworker.kafka.model.ScrapperRequest;

public interface MessageHandlerService {
    void handle(ScrapperRequest request);
}
