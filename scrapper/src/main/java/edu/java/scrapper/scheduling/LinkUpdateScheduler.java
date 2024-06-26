package edu.java.scrapper.scheduling;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.adapters.LinkInfoAdapter;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import edu.java.scrapper.scheduling.handlers.AbstractDomainHandler;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.scheduler", name = "enable")
@RequiredArgsConstructor
public class LinkUpdateScheduler {
    private final ApplicationConfig applicationConfig;
    private final LinkInfoAdapter linkInfoAdapter;
    private final AbstractDomainHandler domainHandler;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        for (LinkInfoDto linkDto : linkInfoAdapter.findAllCheckedAtBefore(OffsetDateTime.now()
            .minus(applicationConfig.scheduler().linkCheckingFrequency()))) {
            try {
                domainHandler.handle(linkDto);
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
    }

}
