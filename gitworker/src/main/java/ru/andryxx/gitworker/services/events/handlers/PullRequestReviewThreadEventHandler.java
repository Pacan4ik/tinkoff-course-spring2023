package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.PullRequestReviewThreadEventPayload;

@Service
public class PullRequestReviewThreadEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "PullRequestReviewThreadEvent";
    private static final String PULL_REQUEST_REVIEW_THREAD_DEFAULT = "Тред в PR %s %s помечен %s";

    public PullRequestReviewThreadEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        PullRequestReviewThreadEventPayload payload = convertTo(event, PullRequestReviewThreadEventPayload.class);
        return String.format(
            PULL_REQUEST_REVIEW_THREAD_DEFAULT,
            payload.pullRequest().title(),
            payload.pullRequest().htmlUrl(),
            payload.action()
        );
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
