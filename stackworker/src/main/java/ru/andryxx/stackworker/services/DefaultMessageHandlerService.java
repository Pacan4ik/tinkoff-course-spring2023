package ru.andryxx.stackworker.services;

import jakarta.transaction.Transactional;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.stackworker.client.StackOverFlowResponse;
import ru.andryxx.stackworker.domain.dao.StackEntityRepository;
import ru.andryxx.stackworker.domain.model.StackEntity;
import ru.andryxx.stackworker.kafka.QueueProducer;
import ru.andryxx.stackworker.kafka.model.ScrapperRequest;
import ru.andryxx.stackworker.kafka.model.WorkerMessage;
import ru.andryxx.stackworker.services.handlers.AbstractEventHandler;
import ru.andryxx.stackworker.services.handlers.exceptions.NoSuitableHandlersException;

@Service
@Slf4j
public class DefaultMessageHandlerService implements MessageHandlerService {
    private final StackEntityRepository stackEntityRepository;
    private final AbstractEventHandler stackChain;
    private final StackFetcher stackFetcher;
    private final QueueProducer queueProducer;

    public DefaultMessageHandlerService(
        StackEntityRepository stackEntityRepository,
        @Qualifier("stackChain") AbstractEventHandler stackChain, StackFetcher stackFetcher, QueueProducer queueProducer
    ) {
        this.stackEntityRepository = stackEntityRepository;
        this.stackChain = stackChain;
        this.stackFetcher = stackFetcher;
        this.queueProducer = queueProducer;
    }

    @Override
    @Transactional
    public void handle(ScrapperRequest request) {
        long stackId = getId(request.url());

        StackOverFlowResponse response = stackFetcher.fetch(stackId);

        Optional<StackEntity> optional = stackEntityRepository.findById(request.id());
        StackEntity stackEntity;
        if (optional.isEmpty()) {
            stackEntity = new StackEntity();
            stackEntity.setId(request.id());
            log.info("Stack entity created: {}", stackEntity);
            log.info("No handlers needed");
        } else {
            stackEntity = optional.get();
            try {
                List<String> messages = stackChain.handle(response, stackEntity, new ArrayList<>());
                String result = buildString(messages);
                if (!result.isEmpty()) {
                    queueProducer.send(new WorkerMessage(stackEntity.getId(), request.url(), result));
                }

            } catch (NoSuitableHandlersException e) {
                log.warn("No suitable handlers found");
                log.info("Probably no changes");
            }
        }

        stackEntity.setAnswerCount(response.items().getFirst().answerCount());
        stackEntity.setCommentCount(response.items().getFirst().commentCount());
        stackEntity.setLastActivityDate(response.items().getFirst().lastActivityDate());
        stackEntityRepository.save(stackEntity);

    }

    private static @NotNull String buildString(List<String> messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append("\n");
        }
        return sb.toString();
    }

    public static long getId(URI url) {
        String[] parts = url.getPath().split("/");
        return Long.parseLong(parts[parts.length - 2]);
    }
}
