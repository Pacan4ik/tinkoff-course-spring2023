package edu.java.scrapper.api.services;

import edu.java.scrapper.ChatRepository;
import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import java.net.URI;
import java.util.Collection;
import org.springframework.stereotype.Service;

@Service
public class DefaultLinkRepositoryService implements LinkRepositoryService {

    //TODO
    private final ChatRepository chatRepository;
    private static final String CHAT_NOT_FOUND = "Chat not found";

    public DefaultLinkRepositoryService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Collection<URI> getUserLinks(Long id) {
        var links = chatRepository.getUserLinks(id);
        if (links == null) {
            throw new ResourceNotFoundException(CHAT_NOT_FOUND);
        }
        return links;
    }

    @Override
    public URI addLink(Long id, URI link) {
        if (!chatRepository.addLink(id, link)) {
            throw new ResourceNotFoundException(CHAT_NOT_FOUND);
        }
        return link;
    }

    @Override
    public URI removeLink(Long id, URI link) {
        if (!chatRepository.checkUser(id)) {
            throw new ResourceNotFoundException(CHAT_NOT_FOUND);
        }
        if (!chatRepository.deleteLink(id, link)) {
            throw new ResourceNotFoundException("Link not found");
        }
        return link;
    }
}
