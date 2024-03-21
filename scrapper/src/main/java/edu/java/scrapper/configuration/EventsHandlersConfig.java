package edu.java.scrapper.configuration;

import edu.java.scrapper.botClient.BotClient;
import edu.java.scrapper.clients.github.GitHubClient;
import edu.java.scrapper.clients.github.GitHubResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverFlowResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverflowClient;
import edu.java.scrapper.domain.jdbc.dao.LinkRepository;
import edu.java.scrapper.scheduling.handlers.AbstractAdditionalHandler;
import edu.java.scrapper.scheduling.handlers.AbstractDomainHandler;
import edu.java.scrapper.scheduling.handlers.github.GitHubHandler;
import edu.java.scrapper.scheduling.handlers.github.OpenIssuesHandler;
import edu.java.scrapper.scheduling.handlers.github.PushedAtHandler;
import edu.java.scrapper.scheduling.handlers.github.UpdatedAtHandler;
import edu.java.scrapper.scheduling.handlers.stackoverflow.AnswerCountHandler;
import edu.java.scrapper.scheduling.handlers.stackoverflow.CommentCountHandler;
import edu.java.scrapper.scheduling.handlers.stackoverflow.LastActivityHandler;
import edu.java.scrapper.scheduling.handlers.stackoverflow.StackOverflowHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EventsHandlersConfig {
    private final BotClient botClient;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final LinkRepository linkRepository;

    @Bean
    public AbstractDomainHandler firstHandler() {
        GitHubHandler gitHubHandler = new GitHubHandler(botClient, linkRepository, gitHubClient, createGitHubChain());
        StackOverflowHandler stackOverflowHandler =
            new StackOverflowHandler(botClient, linkRepository, stackOverflowClient, createStackOverflowChain());
        gitHubHandler.setNextSuccessor(stackOverflowHandler);
        return gitHubHandler;
    }

    public AbstractAdditionalHandler<GitHubResponse> createGitHubChain() {
        OpenIssuesHandler openIssuesHandler = new OpenIssuesHandler();
        PushedAtHandler pushedAtHandler = new PushedAtHandler();
        UpdatedAtHandler updatedAtHandler = new UpdatedAtHandler();

        pushedAtHandler.setNextSuccessor(updatedAtHandler);
        openIssuesHandler.setNextSuccessor(pushedAtHandler);
        return openIssuesHandler;
    }

    public AbstractAdditionalHandler<StackOverFlowResponse> createStackOverflowChain() {
        AnswerCountHandler answerCountHandler = new AnswerCountHandler();
        CommentCountHandler commentCountHandler = new CommentCountHandler();
        LastActivityHandler lastActivityHandler = new LastActivityHandler();

        commentCountHandler.setNextSuccessor(lastActivityHandler);
        answerCountHandler.setNextSuccessor(commentCountHandler);
        return answerCountHandler;
    }
}
