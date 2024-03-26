package edu.java.scrapper.scheduling.handlers.stackoverflow;

import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.domain.dto.LinkDto;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AdditionalHandlerResult;

public class CommentCountHandler extends AbstractAdditionalHandler<StackOverFlowResponse> {

    public static final String COMMENT_COUNT = "comment_count";
    public static final String NEW_COMMENT_MESSAGE = "Появился новый комментарий";

    @Override
    public AdditionalHandlerResult handle(
        StackOverFlowResponse stackOverFlowResponse,
        LinkDto linkDto,
        AdditionalHandlerResult result
    ) {
        Long commentCountResponse = stackOverFlowResponse.items().getFirst().commentCount();
        processNumber(commentCountResponse, COMMENT_COUNT, linkDto, NEW_COMMENT_MESSAGE, result);
        return handleNext(stackOverFlowResponse, linkDto, result);
    }
}
