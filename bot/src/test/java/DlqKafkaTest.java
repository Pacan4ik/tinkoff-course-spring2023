import edu.java.bot.api.services.UpdateHandlerService;
import edu.java.bot.configuration.KafkaConfig;
import edu.java.bot.kafka.UpdatesListener;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = KafkaConfig.class)
@Import({DlqKafkaTest.TestConfig.class, UpdatesListener.class})
@Slf4j
public class DlqKafkaTest extends KafkaContainerEnvironment {

    @Value("${app.kafka.consumer-topic}")
    String topicName;

    @Autowired
    UpdatesListener updatesListener;

    @Test
    @Disabled
    public void shouldSendToDlq() throws InterruptedException {
        //given
        KAFKA_TEMPLATE.send(topicName, "{\"invalid\":\"message\"}");
        Thread.sleep(2000); //wait

        //when
        ConsumerRecords<String, String> polled;
        try (Consumer<String, String> consumer = CONSUMER_FACTORY.createConsumer()) {
            consumer.subscribe(List.of(topicName + "_dlq"));
            polled = consumer.poll(Duration.ofSeconds(5));
        }

        //then
        List<ConsumerRecord<String, String>> polledList = new ArrayList<>();
        polled.iterator().forEachRemaining(polledList::add);

        Assertions.assertEquals(1, polledList.size());
        Assertions.assertEquals("{\"invalid\":\"message\"}", polledList.getFirst().value());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean UpdateHandlerService mocked() {
            return (url, description, ids) -> log.info("update handled");
        }
    }
}
