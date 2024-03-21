package edu.java.scrapper.api.services.jpa;

import edu.java.scrapper.api.exceptions.LinkAlreadyExistsException;
import edu.java.scrapper.api.exceptions.ResourceNotFoundException;
import edu.java.scrapper.api.model.LinkResponse;
import edu.java.scrapper.api.services.LinkService;
import edu.java.scrapper.domain.jpa.dao.ChatRepository;
import edu.java.scrapper.domain.jpa.dao.LinkRepository;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.domain.jpa.model.Link;
import java.net.URI;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class JpaLinkService implements LinkService {
    public static final String CHAT_NOT_FOUND = "Chat not found";
    public static final String USER_ALREADY_SUBSCRIBED = "User already subscribed";
    public static final String LINK_NOT_FOUND = "Link not found";
    public static final String USER_HAS_NOT_SUBSCRIBED = "User has not yet subscribed to this link";
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;

    public JpaLinkService(ChatRepository chatRepository, LinkRepository linkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Override
    @Transactional
    public List<LinkResponse> getUserLinks(Long id) {
        Chat chat = chatRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(CHAT_NOT_FOUND));
        return chat.getSubscribedLinks().stream()
            .map((link) -> new LinkResponse(link.getId(), URI.create(link.getUrl())))
            .toList();
    }

    @Override
    @Transactional
    public LinkResponse addLink(Long id, URI url) {
        Chat chat = chatRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(CHAT_NOT_FOUND));
        Link link = linkRepository.findByUrl(url.toString())
            .orElseGet(() -> {
                    Link newLink = new Link();
                    newLink.setUrl(url.toString());
                    return linkRepository.saveAndFlush(newLink);
                }
            );

        if (chat.getSubscribedLinks().add(link)) {
            chatRepository.saveAndFlush(chat);
        } else {
            throw new LinkAlreadyExistsException(USER_ALREADY_SUBSCRIBED);
        }

        return new LinkResponse(link.getId(), URI.create(link.getUrl()));
    }

    @Override
    @Transactional
    public LinkResponse removeLink(Long id, URI url) {
        Chat chat = chatRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(CHAT_NOT_FOUND));
        Link link = linkRepository.findByUrl(url.toString())
            .orElseThrow(() -> new ResourceNotFoundException(LINK_NOT_FOUND));
        if (chat.getSubscribedLinks().remove(link)) {
            chatRepository.saveAndFlush(chat);
            if (link.getSubscribedChats().isEmpty()) {
                linkRepository.delete(link);
                linkRepository.flush();
            }
        } else {
            throw new ResourceNotFoundException(USER_HAS_NOT_SUBSCRIBED);
        }
        return new LinkResponse(link.getId(), URI.create(link.getUrl()));
    }
}
