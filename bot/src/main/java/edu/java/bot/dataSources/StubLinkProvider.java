package edu.java.bot.dataSources;

import edu.java.bot.utils.url.ParsedUrl;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StubLinkProvider implements SupportedLinkProvider {
    private static final List<Pattern> SUPPORTED_HOSTS = List.of(
        Pattern.compile("^https://stackoverflow\\.com/questions/\\d+(/[^/\\s]+)?$"),
        Pattern.compile("^https://github\\.com/[^/\\s]+/[^/\\s]+$")
    );

    @Override
    public boolean isSupports(ParsedUrl parsedUrl) {
        for (Pattern pattern : SUPPORTED_HOSTS) {
            if (pattern.matcher(parsedUrl.toString()).matches()) {
                return true;
            }
        }
        return false;
    }
}
