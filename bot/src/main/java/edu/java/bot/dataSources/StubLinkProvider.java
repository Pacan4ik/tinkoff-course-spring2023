package edu.java.bot.dataSources;

import edu.java.bot.utils.url.ParsedUrl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class StubLinkProvider implements SupportedLinkProvider {
    @Override
    @Cacheable("supportedLinks")
    public List<ParsedUrl> getSupportedLinks() {
        return List.of(
            new ParsedUrl("https", "google.com", null, null, null),
            new ParsedUrl("https", "github.com", null, "/sanyarnd", null)
        );
    }
}
