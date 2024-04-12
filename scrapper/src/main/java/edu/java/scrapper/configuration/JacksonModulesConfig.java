package edu.java.scrapper.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.java.scrapper.domain.adapters.LinkInfoDto;
import edu.java.scrapper.domain.adapters.serializing.AdditionalInfoDeserializer;
import edu.java.scrapper.domain.adapters.serializing.AdditionalInfoSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonModulesConfig {
    @Bean
    public Module additionalLinkInfoModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LinkInfoDto.AdditionalInfo.class, new AdditionalInfoSerializer());
        module.addDeserializer(LinkInfoDto.AdditionalInfo.class, new AdditionalInfoDeserializer());
        return module;
    }
}
