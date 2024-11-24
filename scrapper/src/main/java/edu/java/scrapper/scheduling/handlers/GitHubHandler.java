package edu.java.scrapper.scheduling.handlers;

import edu.java.scrapper.domain.adapters.LinkDto;
import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import edu.java.scrapper.kafka.producers.workers.WorkerCheckRequest;
import edu.java.scrapper.kafka.producers.workers.WorkerQueueProducer;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GitHubHandler extends AbstractDomainHandler {
    private final WorkerQueueProducer workerQueueProducer;
    private static final String HOST = "github.com";

    public GitHubHandler(
        LinkInfoAdapter linkInfoAdapter,
        WorkerQueueProducer workerQueueProducer
    ) {
        super(linkInfoAdapter);
        this.workerQueueProducer = workerQueueProducer;
    }

    @Override
    protected boolean isSuitableHost(URI url) {
        return url.getHost().equals(HOST);
    }

    @Override
    protected void process(LinkDto linkDto) {
        workerQueueProducer.send(new WorkerCheckRequest(linkDto.getId(), linkDto.getUrl()));
    }
}
