package edu.java.scrapper;

import edu.java.scrapper.configuration.ApplicationConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class LinkUpdateScheduler {
    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        log.info("updated");
    }
}
