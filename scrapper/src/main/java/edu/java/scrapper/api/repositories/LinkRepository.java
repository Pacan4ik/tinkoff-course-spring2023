package edu.java.scrapper.api.repositories;

import java.net.URI;
import java.util.List;

public interface LinkRepository {
    List<URI> getLinks(Long chatId);

    URI addLink(Long chatId, URI url);

    URI delete(Long chatId, URI url);
}
