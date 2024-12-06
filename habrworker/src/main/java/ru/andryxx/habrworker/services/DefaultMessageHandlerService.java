package ru.andryxx.habrworker.services;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.habrworker.domain.dao.HabrEntityRepository;
import ru.andryxx.habrworker.domain.model.HabrEntity;
import ru.andryxx.habrworker.kafka.QueueProducer;
import ru.andryxx.habrworker.kafka.model.ScrapperRequest;
import ru.andryxx.habrworker.kafka.model.WorkerMessage;
import ru.andryxx.habrworker.services.fetchers.HabrFetcher;
import ru.andryxx.habrworker.services.fetchers.model.HabrDTO;
import ru.andryxx.habrworker.services.handlers.AbstractHandler;
import ru.andryxx.habrworker.services.handlers.exceptions.NoSuitableHandlersException;

@Service
@Slf4j
public class DefaultMessageHandlerService implements MessageHandlerService {
    private final HabrEntityRepository habrEntityRepository;
    private final HabrFetcher habrFetcher;
    private final AbstractHandler habrChain;
    private final QueueProducer queueProducer;

    public DefaultMessageHandlerService(
        HabrEntityRepository habrEntityRepository,
        HabrFetcher habrFetcher,
        @Qualifier("habrChain")
        AbstractHandler habrChain, QueueProducer queueProducer
    ) {
        this.habrEntityRepository = habrEntityRepository;
        this.habrFetcher = habrFetcher;
        this.habrChain = habrChain;
        this.queueProducer = queueProducer;
    }

    @Override
    @Transactional
    public void handle(ScrapperRequest request) {
        HabrDTO response = habrFetcher.fetch(request.url());

        Optional<HabrEntity> optional = habrEntityRepository.findById(request.id());
        HabrEntity entity;
        if (optional.isEmpty()) {
            entity = new HabrEntity();
            entity.setId(request.id());
            log.info("Habr entity created: {}", entity);
        } else {
            entity = optional.get();

            try {
                List<String> messages = habrChain.handle(response, entity, new ArrayList<>());
                String result = buildString(messages);
                if (!result.isEmpty()) {
                    queueProducer.send(new WorkerMessage(entity.getId(), request.url(), result));
                }

            } catch (NoSuitableHandlersException e) {
                log.warn("No suitable handlers found");
                log.info("Probably no changes");
            }
        }

        entity.setCommentCount(response.commentCount());
        entity.setContentSha1Hash(response.sha1Hash());
        habrEntityRepository.save(entity);
    }

    private static @NotNull String buildString(List<String> messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append("\n");
        }
        return sb.toString();
    }
}
