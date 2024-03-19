package edu.java.scrapper.api.services;

import java.net.URI;
import java.util.Collection;

public interface LinkRepositoryService {
    Collection<URI> getUserLinks(Long id);

    URI addLink(Long id, URI link);

    URI removeLink(Long id, URI link);
}
