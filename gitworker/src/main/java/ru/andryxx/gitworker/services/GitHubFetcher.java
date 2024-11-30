package ru.andryxx.gitworker.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.andryxx.gitworker.client.GitHubClient;
import ru.andryxx.gitworker.client.GitHubEventResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubFetcher {
    public static final String FAILED_TO_FETCH_EVENTS_FOR_PAGE = "Failed to fetch events for {}/{} page {}";
    public static final String FAILED_TO_FETCH_ONE_EVENT_FOR = "Failed to fetch one event for {}/{} ";
    private final GitHubClient gitHubClient;

    public List<GitHubEventResponse> fetchEventsUntilId(String user, String repos, long id) {
        List<GitHubEventResponse> responses = new ArrayList<>();
        int pagenum = 1;
        for (boolean isFound = false; !isFound && pagenum <= 10; pagenum++) {
            try {
                List<GitHubEventResponse> events = gitHubClient.fetchResponse(user, repos, pagenum);
                for (GitHubEventResponse event : events) {
                    if (event.id() == id) {
                        isFound = true;
                        break;
                    }
                    responses.add(event);
                }
            } catch (Exception e) {
                log.warn(FAILED_TO_FETCH_EVENTS_FOR_PAGE, user, repos, pagenum);
                log.warn(e.toString());
            }
        }
        return responses;
    }

    @Nullable
    public GitHubEventResponse fetchOneEvent(String user, String repos) {
        try {
            return gitHubClient.fetchResponse(user, repos, 1).getFirst();
        } catch (Exception e) {
            log.warn(FAILED_TO_FETCH_ONE_EVENT_FOR, user, repos);
            log.warn(e.toString());
            return null;
        }
    }

}
