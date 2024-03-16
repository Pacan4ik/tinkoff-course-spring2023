package edu.java.scrapper.scheduling.handlers.github;

import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.domain.dto.LinkDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.time.OffsetDateTime;

public class UpdatedAtHandler extends AbstractAdditionalHandler<GitHubResponse> {
    @Override
    public AdditionalHandlerResult handle(
        GitHubResponse gitHubResponse,
        LinkDto linkDto,
        AdditionalHandlerResult result
    ) {
        OffsetDateTime updatedAt = linkDto.updatedAt();
        if (updatedAt.isBefore(gitHubResponse.updatedAt())) {
            result.setRowUpdateConsumer(
                result.getRowUpdateConsumer()
                    .andThen(linkRepository -> linkRepository.updateUpdatedAt(linkDto.id(), gitHubResponse.updatedAt()))
            );

            if (result.getDescriptions().isEmpty()) {
                result.getDescriptions().add("Новое обновление");
            }
        }
        return handleNext(gitHubResponse, linkDto, result);
    }
}
