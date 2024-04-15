package edu.java.scrapper.scheduling.handlers.stackoverflow;

import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;
import java.util.Objects;

public class CommentCountHandler extends AbstractAdditionalHandler<StackOverFlowResponse> {
    public static final String NEW_COMMENT_MESSAGE = "Появился новый комментарий";

    @Override
    public AdditionalHandlerResult handle(
        StackOverFlowResponse stackOverFlowResponse,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        return handleNext(
            stackOverFlowResponse,
            additionalInfo,
            processComments(stackOverFlowResponse, additionalInfo, result)
        );
    }

    private AdditionalHandlerResult processComments(
        StackOverFlowResponse response,
        LinkInfoDto.AdditionalInfo additionalInfo,
        AdditionalHandlerResult result
    ) {
        Long responseComments;
        try {
            responseComments = Objects.requireNonNull(response.items().getFirst().commentCount());
        } catch (NullPointerException e) {
            return result;
        }

        Long dtoInfoComments = additionalInfo.getCommentCount();

        if (!responseComments.equals(dtoInfoComments)) {
            result.setAdditionalInfoConsumer(addToConsumer(
                result,
                info -> info.setCommentCount(responseComments)
            ));

            if (dtoInfoComments != null && responseComments > dtoInfoComments) {
                result.getDescriptions().add(NEW_COMMENT_MESSAGE);
            }
        }
        return result;
    }
}
