package edu.java.scrapper.kafka;

import edu.java.scrapper.configuration.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = KafkaConfig.class)
@Slf4j
public class EventHandlersTest extends KafkaContainerEnvironment {
    //too complicated
}
