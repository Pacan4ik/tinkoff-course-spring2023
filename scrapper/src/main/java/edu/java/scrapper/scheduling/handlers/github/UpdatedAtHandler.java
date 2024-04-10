package edu.java.scrapper.scheduling.handlers.github;

import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.time.OffsetDateTime;

public class UpdatedAtHandler extends AbstractAdditionalHandler<GitHubResponse> {

    public static final String UPDATE_MESSAGE = "Новое обновление";

    @Override
    public AdditionalHandlerResult handle(
        GitHubResponse gitHubResponse,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        return handleNext(gitHubResponse, additionalInfo, processUpdatedAt(gitHubResponse, additionalInfo, result));
    }

    private AdditionalHandlerResult processUpdatedAt(
        GitHubResponse response,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        OffsetDateTime responseUpdatedAt = response.updatedAt();
        OffsetDateTime dtoInfoPushedAt = additionalInfo.getUpdatedAt();

        if (responseUpdatedAt == null) {
            return result;
        }

        if (!responseUpdatedAt.equals(dtoInfoPushedAt)) {
            result.setAdditionalInfoConsumer(addToConsumer(
                result,
                info -> info.setUpdatedAt(responseUpdatedAt)
            ));

            if (dtoInfoPushedAt != null && responseUpdatedAt.isAfter(dtoInfoPushedAt)
                && result.getDescriptions().isEmpty()) {
                result.getDescriptions().add(UPDATE_MESSAGE);
            }
        }
        return result;
    }
}
