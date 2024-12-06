package ru.andryxx.gitworker.configuration.event;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlersObjectMapperConfig {
    private final ObjectMapper objectMapper;

    //CHECKSTYLE:OFF
    public HandlersObjectMapperConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        this.objectMapper = objectMapper;
    }
    //CHECKSTYLE:ON

    @Bean("handlersObjectMapper")
    public ObjectMapper handlersObjectMapper() {
        return objectMapper;
    }

}
