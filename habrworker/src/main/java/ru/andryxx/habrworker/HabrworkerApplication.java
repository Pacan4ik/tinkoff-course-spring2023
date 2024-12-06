package ru.andryxx.habrworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.andryxx.habrworker.configuration.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class HabrworkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabrworkerApplication.class, args);
    }

}
