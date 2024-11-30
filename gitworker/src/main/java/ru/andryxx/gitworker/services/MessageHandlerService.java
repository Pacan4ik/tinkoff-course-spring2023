package ru.andryxx.gitworker.services;

import ru.andryxx.gitworker.model.ScrapperRequest;

public interface MessageHandlerService {
    void handle(ScrapperRequest request);
}
