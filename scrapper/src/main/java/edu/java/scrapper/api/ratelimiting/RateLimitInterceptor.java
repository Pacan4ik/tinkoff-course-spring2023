package edu.java.scrapper.api.ratelimiting;

import edu.java.scrapper.api.exceptions.BadRequestException;
import edu.java.scrapper.api.exceptions.OutOfTokensException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {
    private final Map<String, Bucket> bucketMap = new ConcurrentHashMap<>();
    private final Long rateLimit;
    private final Duration fullRefill;

    public RateLimitInterceptor(Long rateLimit, Duration fullRefill) {
        this.rateLimit = rateLimit;
        this.fullRefill = fullRefill;
    }

    private Bucket newBucket(String key) {
        Refill refill = Refill.greedy(rateLimit, fullRefill);
        Bandwidth limit = Bandwidth.classic(rateLimit, refill);
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }

    @Override
    public boolean preHandle(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull Object handler
    ) {
        String ip = request.getRemoteAddr();
        if (ip == null || ip.isEmpty()) {
            throw new BadRequestException("IP is missing");
        }
        Bucket bucket = bucketMap.computeIfAbsent(ip, this::newBucket);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1L);
        if (!probe.isConsumed()) {
            throw new OutOfTokensException(
                String.format(
                    "Too many requests. Wait %d seconds before new request.",
                    Duration.ofNanos(probe.getNanosToWaitForRefill()).toSeconds()
                )
            );
        }
        response.setHeader("Remaining-Tokens", String.valueOf(probe.getRemainingTokens()));
        return true;
    }
}
