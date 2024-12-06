package ru.andryxx.gitworker.services.events.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import ru.andryxx.gitworker.services.events.handlers.schema.PullRequestEventPayload;

@Service
public class PullRequestEventHandler extends AbstractEventHandler {
    private static final String EVENT_TYPE = "PullRequestEvent";
    private static final String PULL_REQUEST_DEFAULT = "PR %s %s приобрел статус %s";

    public PullRequestEventHandler(@Qualifier("handlersObjectMapper") ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String process(GitHubEventResponse event) {
        PullRequestEventPayload payload = convertTo(event, PullRequestEventPayload.class);
        return String.format(
            PULL_REQUEST_DEFAULT,
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
