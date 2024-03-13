package edu.java.scrapper.api.services.jdbc;

import edu.java.scrapper.api.repositories.LinkRepository;
import edu.java.scrapper.api.services.LinkRepositoryService;
import java.net.URI;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkRepositoryService implements LinkRepositoryService {
    private final LinkRepository linkRepository;

    public JdbcLinkRepositoryService(@Qualifier("jdbcLinkRepository") LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public Collection<URI> getUserLinks(Long id) {
        return linkRepository.getLinks(id);
    }

    @Override
    public URI addLink(Long id, URI link) {
        return linkRepository.addLink(id, link);
    }

    @Override
    public URI removeLink(Long id, URI link) {
        return linkRepository.delete(id, link);
    }
}
