package ru.andryxx.gitworker.configuration;

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
import ru.andryxx.gitworker.kafka.model.WorkerMessage;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private final ApplicationConfig applicationConfig;

    @Bean
    public ProducerFactory<String, WorkerMessage> producerFactory() {
        Map<String, Object> configs = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, WorkerMessage>
    workerGitKafkaTemplate(ProducerFactory<String, WorkerMessage> producerFactory) {
        KafkaTemplate<String, WorkerMessage> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(applicationConfig.kafka().producerTopic());
        return kafkaTemplate;
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

    @Bean
    public ProducerFactory<String, String> dlqProducerFactory() {
        Map<String, Object> configs = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configs);

    }

    @Bean("dlqKafkaTemplate")
    public KafkaTemplate<String, String>
    kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
