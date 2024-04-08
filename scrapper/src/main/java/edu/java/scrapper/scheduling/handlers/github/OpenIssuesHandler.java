package edu.java.scrapper.scheduling.handlers.github;

import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.domain.dto.LinkDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;

public class OpenIssuesHandler extends AbstractAdditionalHandler<GitHubResponse> {

    public static final String OPEN_ISSUES_COUNT = "open_issues_count";
    public static final String NEW_TICKET_MESSAGE = "Появился новый тикет";

    @Override
    public AdditionalHandlerResult handle(
        GitHubResponse gitHubResponse,
        LinkDto linkDto,
        AdditionalHandlerResult result
    ) {
        return handleNext(
            gitHubResponse,
            linkDto,
            processNumber(gitHubResponse.openIssuesCount(), OPEN_ISSUES_COUNT, linkDto, NEW_TICKET_MESSAGE, result)
        );
    }
}
