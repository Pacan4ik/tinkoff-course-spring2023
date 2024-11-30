package ru.andryxx.gitworker.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.domain.dao.GitEntityRepository;
import ru.andryxx.gitworker.domain.model.GitEntity;
import ru.andryxx.gitworker.kafka.QueueProducer;
import ru.andryxx.gitworker.kafka.model.ScrapperRequest;
import ru.andryxx.gitworker.kafka.model.WorkerMessage;
import ru.andryxx.gitworker.services.events.EventsProcessor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultMessageHandlerService implements MessageHandlerService {
    public static final String FAILED_TO_FETCH_EVENTS = "Failed to fetch events";
    private final GitEntityRepository gitEntityRepository;
    private final GitHubFetcher gitHubFetcher;
    private final EventsProcessor eventsProcessor;
    private final QueueProducer queueProducer;

    @Override
    @Transactional
    public void handle(ScrapperRequest request) {
        GitEntity gitEntity;
        Optional<GitEntity> optional = gitEntityRepository.findById(request.id());
        if (optional.isPresent()) {
            gitEntity = optional.get();
        } else {
            gitEntity = new GitEntity();
            gitEntity.setId(request.id());
            gitEntity = gitEntityRepository.save(gitEntity);
        }

        UserRepositoryUtils.UserRepositoryRecord parsed = UserRepositoryUtils.parse(request.url().toString());
        try {
            if (gitEntity.getLastEventId() == null) {
                GitHubEventResponse lastResponse = gitHubFetcher.fetchOneEvent(parsed.owner(), parsed.repo());
                Objects.requireNonNull(lastResponse);
                gitEntity.setLastEventId(lastResponse.id());
                gitEntityRepository.save(gitEntity);
            } else {
                List<GitHubEventResponse> gitHubEventResponses =
                    gitHubFetcher.fetchEventsUntilId(parsed.owner(), parsed.repo(), gitEntity.getLastEventId());
                if (gitHubEventResponses.isEmpty()) {
                    return;
                }
                String eventsMessage = eventsProcessor.processEvents(gitHubEventResponses);
                if (eventsMessage != null) {
                    queueProducer.send(new WorkerMessage(gitEntity.getId(), request.url(), eventsMessage));
                }

                gitEntity.setLastEventId(gitHubEventResponses.getFirst().id());
                gitEntityRepository.save(gitEntity);
            }
        } catch (Exception e) {
            log.warn(FAILED_TO_FETCH_EVENTS, e);
        }
    }

}
