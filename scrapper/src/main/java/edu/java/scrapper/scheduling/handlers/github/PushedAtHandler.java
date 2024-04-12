package edu.java.scrapper.scheduling.handlers.github;

import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.time.OffsetDateTime;

public class PushedAtHandler extends AbstractAdditionalHandler<GitHubResponse> {
    public static final String NEW_COMMIT_MESSAGE = "Появился новый коммит";

    @Override
    public AdditionalHandlerResult handle(
        GitHubResponse gitHubResponse,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        return handleNext(gitHubResponse, additionalInfo, processPushedAt(gitHubResponse, additionalInfo, result));
    }

    private AdditionalHandlerResult processPushedAt(
        GitHubResponse response,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        OffsetDateTime responsePushedAt = response.pushedAt();
        OffsetDateTime dtoInfoPushedAt = additionalInfo.getPushedAt();

        if (responsePushedAt == null) {
            return result;
        }

        if (!responsePushedAt.equals(dtoInfoPushedAt)) {
            result.setAdditionalInfoConsumer(addToConsumer(
                result,
                info -> info.setPushedAt(responsePushedAt)
            ));

            if (dtoInfoPushedAt != null && responsePushedAt.isAfter(dtoInfoPushedAt)) {
                result.getDescriptions().add(NEW_COMMIT_MESSAGE);
            }
        }
        return result;
    }

}
