package edu.java.scrapper.scheduling.handlers.stackoverflow;

import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.domain.dto.LinkDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.time.OffsetDateTime;

public class LastActivityHandler extends AbstractAdditionalHandler<StackOverFlowResponse> {
    @Override
    public AdditionalHandlerResult handle(
        StackOverFlowResponse stackOverFlowResponse,
        LinkDto linkDto,
        AdditionalHandlerResult result
    ) {
        OffsetDateTime lastActivity = stackOverFlowResponse.items().getLast().lastActivityDate();
        OffsetDateTime updatedAt = linkDto.updatedAt();
        if (updatedAt.isBefore(lastActivity)) {
            result.setRowUpdateConsumer(
                result.getRowUpdateConsumer()
                    .andThen(linkRepository -> linkRepository.updateUpdatedAt(linkDto.id(), lastActivity))
            );

            if (result.getDescriptions().isEmpty()) {
                result.getDescriptions().add("Новое обновление");
            }
        }
        return handleNext(stackOverFlowResponse, linkDto, result);
    }
}
