package edu.java.scrapper.scheduling.handlers.stackoverflow;

import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.time.OffsetDateTime;
import java.util.Objects;

public class LastActivityHandler extends AbstractAdditionalHandler<StackOverFlowResponse> {

    public static final String NEW_UPDATE_MESSAGE = "Новое обновление";

    @Override
    public AdditionalHandlerResult handle(
        StackOverFlowResponse stackOverFlowResponse,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        return handleNext(
            stackOverFlowResponse,
            additionalInfo,
            processLastActivity(stackOverFlowResponse, additionalInfo, result)
        );
    }

    private AdditionalHandlerResult processLastActivity(
        StackOverFlowResponse response,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {

        OffsetDateTime responseLastActivity;
        try {
            responseLastActivity = Objects.requireNonNull(response.items().getFirst().lastActivityDate());
        } catch (NullPointerException e) {
            return result;
        }

        OffsetDateTime dtoInfoLastActivity = additionalInfo.getLastActivityDate();

        if (!responseLastActivity.equals(dtoInfoLastActivity)) {
            result.setAdditionalInfoConsumer(addToConsumer(
                result,
                info -> info.setLastActivityDate(responseLastActivity)
            ));

            if (dtoInfoLastActivity != null && responseLastActivity.isAfter(dtoInfoLastActivity)
                && result.getDescriptions().isEmpty()) {
                result.getDescriptions().add(NEW_UPDATE_MESSAGE);
            }
        }
        return result;
    }
}
