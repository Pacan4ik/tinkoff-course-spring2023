package edu.java.scrapper.scheduling.handlers.github;

import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;

public class OpenIssuesHandler extends AbstractAdditionalHandler<GitHubResponse> {
    public static final String NEW_TICKET_MESSAGE = "Появился новый тикет";

    @Override
    public AdditionalHandlerResult handle(
        GitHubResponse gitHubResponse,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        return handleNext(
            gitHubResponse,
            additionalInfo,
            processIssues(gitHubResponse, additionalInfo, result)
        );
    }

    private AdditionalHandlerResult processIssues(
        GitHubResponse response,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        Long responseIssues = response.openIssuesCount();
        Long dtoInfoIssues = additionalInfo.getOpenIssuesCount();

        if (responseIssues == null) {
            return result;
        }

        if (!responseIssues.equals(dtoInfoIssues)) {
            result.setAdditionalInfoConsumer(addToConsumer(
                result,
                info -> info.setOpenIssuesCount(responseIssues)
            ));

            if (dtoInfoIssues != null && responseIssues > dtoInfoIssues) {
                result.getDescriptions().add(NEW_TICKET_MESSAGE);
            }
        }
        return result;

    }
}
