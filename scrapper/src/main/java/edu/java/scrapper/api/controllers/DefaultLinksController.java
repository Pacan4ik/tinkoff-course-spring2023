package edu.java.scrapper.api.controllers;

import edu.java.scrapper.api.model.AddLinkRequest;
import edu.java.scrapper.api.model.LinkResponse;
import edu.java.scrapper.api.model.ListLinksResponse;
import edu.java.scrapper.api.model.RemoveLinkRequest;
import edu.java.scrapper.api.services.LinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultLinksController implements LinksController {
    private final LinkService linkService;

    DefaultLinksController(LinkService linkService) {
        this.linkService = linkService;
    }

    @Override
    public ResponseEntity<ListLinksResponse> getLinks(Long id) {
        ListLinksResponse response = new ListLinksResponse(linkService.getUserLinks(id));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(Long id, AddLinkRequest addLinkRequest) {
        return ResponseEntity.ok(
            linkService.addLink(id, addLinkRequest.link())
        );
    }

    @Override
    public ResponseEntity<LinkResponse> deleteLink(Long id, RemoveLinkRequest removeLinkRequest) {
        return ResponseEntity.ok(
            linkService.removeLink(id, removeLinkRequest.link())
        );
    }
}
