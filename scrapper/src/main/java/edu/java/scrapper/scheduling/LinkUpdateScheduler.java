package edu.java.scrapper.scheduling;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.dao.LinkRepository;
import edu.java.scrapper.domain.dto.LinkDto;
import edu.java.scrapper.scheduling.handlers.AbstractDomainHandler;
import java.time.OffsetDateTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdateScheduler {
    private final ApplicationConfig applicationConfig;
    private final LinkRepository linkRepository;
    private final AbstractDomainHandler domainHandler;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        for (LinkDto linkDto : linkRepository.findAllWhereCheckedAtBefore(OffsetDateTime.now()
            .minus(applicationConfig.scheduler().linkCheckingFrequency()))) {
            try {
                domainHandler.handle(linkDto);
            } catch (Exception e) {
                log.error(e.toString());
                log.error(Arrays.toString(e.getStackTrace()));
            }
        }
    }

}
