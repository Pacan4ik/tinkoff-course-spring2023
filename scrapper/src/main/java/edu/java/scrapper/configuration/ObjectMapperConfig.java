package edu.java.scrapper.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import edu.java.scrapper.domain.adapters.serializing.AdditionalInfoDeserializer;
import edu.java.scrapper.domain.adapters.serializing.AdditionalInfoSerializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    private ObjectMapperConfig() {
    }

    public static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LinkInfoDto.AdditionalInfo.class, new AdditionalInfoSerializer());
        module.addDeserializer(LinkInfoDto.AdditionalInfo.class, new AdditionalInfoDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
