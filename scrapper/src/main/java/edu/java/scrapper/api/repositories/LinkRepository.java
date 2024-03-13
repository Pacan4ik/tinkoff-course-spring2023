package edu.java.scrapper.api.repositories;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    Optional<Long> findLink(URI url);

    List<URI> getLinks(Long chatId);

    URI addLink(Long chatId, URI url);

    URI delete(Long chatId, URI url);

    List<URI> getLinksCheckedBefore(OffsetDateTime offsetDateTime);

    OffsetDateTime updateCheckedAt(URI url, OffsetDateTime newOffsetDateTime);

    OffsetDateTime updateUpdatedAt(URI url, OffsetDateTime newOffsetDateTime);

    OffsetDateTime getLastUpdate(URI url);

    List<Long> getSubscribedChats(URI url);
}
