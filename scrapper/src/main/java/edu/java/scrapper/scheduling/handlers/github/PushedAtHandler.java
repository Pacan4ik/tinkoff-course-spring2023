package edu.java.scrapper.scheduling.handlers.github;

import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.domain.dto.LinkDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.time.OffsetDateTime;
import java.util.Objects;

public class PushedAtHandler extends AbstractAdditionalHandler<GitHubResponse> {

    public static final String PUSHED_AT = "pushed_at";
    public static final String NEW_COMMIT_MESSAGE = "Появился новый коммит";

    @Override
    public AdditionalHandlerResult handle(
        GitHubResponse gitHubResponse,
        LinkDto linkDto,
        AdditionalHandlerResult result
    ) {
        try {
            String pushedAtString = Objects.requireNonNull(linkDto.additionalInfo().findValue(PUSHED_AT)).asText();
            OffsetDateTime pushedAt = OffsetDateTime.parse(pushedAtString);
            if (pushedAt.isBefore(gitHubResponse.pushedAt())) {
                result.setRowUpdateConsumer(
                    addToConsumerUpdateAdditionalInfo(PUSHED_AT, gitHubResponse.pushedAt().toString(), linkDto, result)
                );
                result.getDescriptions().add(NEW_COMMIT_MESSAGE);
            }

        } catch (NullPointerException e) {
            result.setRowUpdateConsumer(
                addToConsumerUpdateAdditionalInfo(PUSHED_AT, gitHubResponse.pushedAt().toString(), linkDto, result)
            );
        }

        return handleNext(gitHubResponse, linkDto, result);
    }

}
