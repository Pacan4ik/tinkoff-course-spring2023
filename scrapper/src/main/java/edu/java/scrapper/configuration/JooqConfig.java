package edu.java.scrapper.configuration;

import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {
    @Bean
    public DefaultConfigurationCustomizer defaultConfigurationCustomizer() {
        return (DefaultConfiguration config) -> config.settings()
            .withBindOffsetDateTimeType(true)
            .withBindOffsetTimeType(true)
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);

    }
}
