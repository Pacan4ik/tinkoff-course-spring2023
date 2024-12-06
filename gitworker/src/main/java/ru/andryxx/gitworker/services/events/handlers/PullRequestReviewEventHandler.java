package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.PullRequestReviewEventPayload;

@Service
public class PullRequestReviewEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "PullRequestReviewEvent";
    private static final String REVIEW_CREATED = "Новый review к PR %s %s";
    private static final String REVIEW_DEFAULT = "Review %s %s %s";
    public static final String CREATED = "created";

    public PullRequestReviewEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        PullRequestReviewEventPayload payload = convertTo(event, PullRequestReviewEventPayload.class);
        if (payload.action().equals(CREATED)) {
            return String.format(
                REVIEW_CREATED,
                payload.pullRequest().title(),
                payload.pullRequest().htmlUrl()
            );
        }
        return String.format(
            REVIEW_DEFAULT,
            payload.action(),
            payload.pullRequest().title(),
            payload.pullRequest().htmlUrl()
        );
    }

    @Override
    protected boolean isSuitableEvent(GitHubEventResponse event) {
        return EVENT_TYPE.equals(event.type());
    }
}
