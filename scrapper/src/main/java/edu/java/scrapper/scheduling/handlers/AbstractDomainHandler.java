package edu.java.scrapper.scheduling.handlers;

import edu.java.scrapper.domain.adapters.LinkDto;
import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import edu.java.scrapper.scheduling.handlers.exceptions.NoSuitableHandlersException;
import java.net.URI;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDomainHandler {
    @Setter
    protected AbstractDomainHandler nextSuccessor;
    protected LinkInfoAdapter linkInfoAdapter;

    public AbstractDomainHandler(
        LinkInfoAdapter linkInfoAdapter
    ) {
        this.linkInfoAdapter = linkInfoAdapter;
    }

    protected abstract boolean isSuitableHost(URI url);

    public final void handle(LinkDto linkDto) throws NoSuitableHandlersException {
        if (!isSuitableHost(linkDto.getUrl())) {
            handleNext(linkDto);
            return;
        }
        process(linkDto);
    }

    protected final void handleNext(LinkDto linkDto) throws NoSuitableHandlersException {
        if (nextSuccessor != null) {
            nextSuccessor.handle(linkDto);
        } else {
            throw new NoSuitableHandlersException("No suitable handler for " + linkDto.getUrl());
        }
    }

    protected abstract void process(LinkDto linkDto);
}
