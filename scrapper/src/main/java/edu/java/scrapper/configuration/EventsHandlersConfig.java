package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import edu.java.scrapper.kafka.producers.workers.GitWorkerQueueProducer;
import edu.java.scrapper.kafka.producers.workers.StackWorkerQueueProducer;
import edu.java.scrapper.scheduling.handlers.AbstractDomainHandler;
import edu.java.scrapper.scheduling.handlers.GitHubHandler;
import edu.java.scrapper.scheduling.handlers.StackOverflowHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EventsHandlersConfig {
    private final GitWorkerQueueProducer gitWorkerQueueProducer;
    private final StackWorkerQueueProducer stackWorkerQueueProducer;
    private final LinkInfoAdapter linkInfoAdapter;

    @Bean
    public AbstractDomainHandler firstHandler() {
        GitHubHandler gitHubHandler =
            new GitHubHandler(
                linkInfoAdapter,
                gitWorkerQueueProducer
            );
        StackOverflowHandler stackOverflowHandler =
            new StackOverflowHandler(
                linkInfoAdapter,
                stackWorkerQueueProducer
            );
        gitHubHandler.setNextSuccessor(stackOverflowHandler);
        return gitHubHandler;
    }

}
