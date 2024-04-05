package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.botClient.BotUpdatesRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final ApplicationConfig applicationConfig;

    @Bean
    public ProducerFactory<String, BotUpdatesRequest> producerFactory() {
        Map<String, Object> configs = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, BotUpdatesRequest>
    kafkaTemplate(ProducerFactory<String, BotUpdatesRequest> producerFactory) {
        KafkaTemplate<String, BotUpdatesRequest> kafkaTemplate =
            new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(applicationConfig.kafka().producerTopic());
        return kafkaTemplate;
    }
}
