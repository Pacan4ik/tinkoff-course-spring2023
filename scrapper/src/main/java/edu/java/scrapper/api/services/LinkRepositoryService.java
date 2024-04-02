package edu.java.scrapper.api.services;

import edu.java.scrapper.api.model.LinkResponse;
import java.net.URI;
import java.util.List;

public interface LinkRepositoryService {
    List<LinkResponse> getUserLinks(Long id);

    LinkResponse addLink(Long id, URI link);

    LinkResponse removeLink(Long id, URI link);
}
