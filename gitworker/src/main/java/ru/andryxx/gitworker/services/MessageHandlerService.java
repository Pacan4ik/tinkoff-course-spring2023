package ru.andryxx.gitworker.services;

import ru.andryxx.gitworker.kafka.model.ScrapperRequest;

public interface MessageHandlerService {
    void handle(ScrapperRequest request);
}
