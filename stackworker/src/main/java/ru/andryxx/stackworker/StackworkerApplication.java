package ru.andryxx.stackworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.andryxx.stackworker.configuration.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class StackworkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StackworkerApplication.class, args);
    }

}
