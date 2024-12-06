package edu.java.scrapper.scheduling.handlers;

import edu.java.scrapper.domain.adapters.LinkDto;
import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import edu.java.scrapper.kafka.producers.workers.WorkerCheckRequest;
import edu.java.scrapper.kafka.producers.workers.WorkerQueueProducer;
import java.net.URI;

public class HabrHandler extends AbstractDomainHandler {
    private final WorkerQueueProducer workerQueueProducer;
    private static final String HOST = "habr.com";

    public HabrHandler(LinkInfoAdapter linkInfoAdapter, WorkerQueueProducer workerQueueProducer) {
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
