package ru.andryxx.habrworker.configuration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.andryxx.habrworker.services.fetchers.HabrHtmlParser;

@Configuration
@RequiredArgsConstructor
public class ParserConfig {
    public static final String SHA_1 = "SHA-1";
    private final ApplicationConfig applicationConfig;

    @Bean
    public HabrHtmlParser habrHtmlParser() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(SHA_1);
        return new HabrHtmlParser(
            applicationConfig.habr().commentSelector(),
            applicationConfig.habr().contentSelector(),
            digest
        );
    }
}
