package edu.java.bot.dataSources;

import edu.java.bot.utils.url.ParsedUrl;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class StubLinkProvider implements SupportedLinkProvider {
    @Override
    @Cacheable("supportedLinks")
    //CHECKSTYLE:OFF: checkstyle:MultipleStringLiterals
    public List<ParsedUrl> getSupportedLinks() {
        return List.of(
            new ParsedUrl("https", "google.com", null, null, null),
            new ParsedUrl("https", "github.com", null, "/sanyarnd", null)
        );
    }
}
