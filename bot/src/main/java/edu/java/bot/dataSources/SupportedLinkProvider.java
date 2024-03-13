package edu.java.bot.dataSources;

import edu.java.bot.utils.url.ParsedUrl;

public interface SupportedLinkProvider {
    boolean isSupports(ParsedUrl parsedUrl);
}
