package edu.java.scrapper.api.controllers;

import edu.java.scrapper.api.model.AddLinkRequest;
import edu.java.scrapper.api.model.LinkResponse;
import edu.java.scrapper.api.model.ListLinksResponse;
import edu.java.scrapper.api.model.RemoveLinkRequest;
import edu.java.scrapper.api.services.LinkRepositoryService;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultLinksController implements LinksController {
    private final LinkRepositoryService linkRepositoryService;

    DefaultLinksController(LinkRepositoryService linkRepositoryService) {
        this.linkRepositoryService = linkRepositoryService;
    }

    @Override
    public ResponseEntity<ListLinksResponse> getLinks(Long id) {
        Collection<URI> uris = linkRepositoryService.getUserLinks(id);

        List<LinkResponse> linkResponses = uris.stream()
            .map(uri -> new LinkResponse(null, uri))
            .collect(Collectors.toList());

        ListLinksResponse response = new ListLinksResponse(linkResponses, linkResponses.size());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(Long id, AddLinkRequest addLinkRequest) {
        return ResponseEntity.ok(new LinkResponse(
                id,
                linkRepositoryService.addLink(id, addLinkRequest.link())
            )
        );
    }

    @Override
    public ResponseEntity<LinkResponse> deleteLink(Long id, RemoveLinkRequest removeLinkRequest) {
        return ResponseEntity.ok(new LinkResponse(
                id,
                linkRepositoryService.removeLink(id, removeLinkRequest.link())
            )
        );
    }
}
