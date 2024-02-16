package edu.java.bot.dataSources;

import edu.java.bot.utils.url.ParsedUrl;
import java.util.List;

public interface SupportedLinkProvider {
    List<ParsedUrl> getSupportedLinks();
}
