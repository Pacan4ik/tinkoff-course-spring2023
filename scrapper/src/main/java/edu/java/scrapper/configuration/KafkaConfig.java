package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.botClient.BotUpdatesRequest;
import edu.java.scrapper.kafka.producers.workers.WorkerCheckRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private final ApplicationConfig applicationConfig;

    @Bean
    public ProducerFactory<String, BotUpdatesRequest> botQueueProducerFactory() {
        Map<String, Object> configs = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public ProducerFactory<String, WorkerCheckRequest> workerQueueProducerFactory() {
        Map<String, Object> configs = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public ProducerFactory<String, String> dlqProducerFactory() {
        Map<String, Object> configs = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configs);

    }

    @Bean
    public KafkaTemplate<String, BotUpdatesRequest>
    botKafkaTemplate(ProducerFactory<String, BotUpdatesRequest> producerFactory) {
        KafkaTemplate<String, BotUpdatesRequest> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(applicationConfig.kafka().botProducerTopic());
        return kafkaTemplate;
    }

    @Bean("gitWorkerProducerFactory")
    public KafkaTemplate<String, WorkerCheckRequest>
    workerGitKafkaTemplate(ProducerFactory<String, WorkerCheckRequest> producerFactory) {
        KafkaTemplate<String, WorkerCheckRequest> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(applicationConfig.kafka().gitworkerProducerTopic());
        return kafkaTemplate;
    }

    @Bean("stackWorkerProducerFactory")
    public KafkaTemplate<String, WorkerCheckRequest>
    workerStackKafkaTemplate(ProducerFactory<String, WorkerCheckRequest> producerFactory) {
        KafkaTemplate<String, WorkerCheckRequest> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(applicationConfig.kafka().stackworkerProducerTopic());
        return kafkaTemplate;
    }

    @Bean("dlqKafkaTemplate")
    public KafkaTemplate<String, String>
    kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configs = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers,

            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,

            ConsumerConfig.GROUP_ID_CONFIG, groupId
        );
        return new DefaultKafkaConsumerFactory<>(
            configs
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    containerFactory(
        ConsumerFactory<String, String> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> container =
            new ConcurrentKafkaListenerContainerFactory<>();
        container.setConcurrency(1);
        container.setConsumerFactory(consumerFactory);
        return container;
    }
}
