package ru.andryxx.gitworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.andryxx.gitworker.configuration.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class GitworkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitworkerApplication.class, args);
    }

}
