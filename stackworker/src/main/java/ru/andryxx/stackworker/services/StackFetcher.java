package ru.andryxx.stackworker.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.andryxx.stackworker.client.StackOverFlowResponse;
import ru.andryxx.stackworker.client.StackOverflowClient;

@Service
@RequiredArgsConstructor
public class StackFetcher {
    private final StackOverflowClient stackOverflowClient;

    public StackOverFlowResponse fetch(long questionId) {
        return stackOverflowClient.fetchResponse(String.valueOf(questionId));
    }
}
