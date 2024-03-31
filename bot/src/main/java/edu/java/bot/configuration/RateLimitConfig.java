package edu.java.bot.configuration;

import edu.java.bot.api.ratelimiting.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RateLimitConfig implements WebMvcConfigurer {
    private final ApplicationConfig applicationConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(
                applicationConfig.rateLimiting().requestsLimit(),
                applicationConfig.rateLimiting().timeDuration()
            ))
            .addPathPatterns("/**");
    }
}
