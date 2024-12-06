package ru.andryxx.habrworker.services.fetchers;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.andryxx.habrworker.client.RawHtmlClient;
import ru.andryxx.habrworker.services.fetchers.model.HabrDTO;

@Service
@RequiredArgsConstructor
public class DefaultHabrFetcher implements HabrFetcher {
    private final RawHtmlClient rawHtmlClient;
    private final HabrHtmlParser habrParser;

    @Override
    public HabrDTO fetch(URI url) {
        try {
            String rawHtml = rawHtmlClient.fetchRawHtmlResponse(url.toString());
            return habrParser.parse(rawHtml);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch data from " + url, e);
        }
    }
}
