package edu.java.scrapper.domain.adapters;

import edu.java.scrapper.domain.jpa.dao.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkInfoAdapter implements LinkInfoAdapter {
    private final LinkRepository linkRepository;

    @Override
    public List<LinkDto> findAllCheckedAtBefore(OffsetDateTime offsetDateTime) {
        return linkRepository.findByCheckedAtLessThan(offsetDateTime).stream()
            .map(link -> new LinkDto(
                link.getId(),
                URI.create(link.getUrl()),
                link.getCheckedAt()
            ))
            .toList();
    }

    @Override
    public void updateCheckedAt(Long id, OffsetDateTime offsetDateTime) {
        linkRepository.updateCheckedAtById(offsetDateTime, id);
    }

    @Override
    public Collection<Long> getSubscribedChats(Long id) {
        return linkRepository.getChatIdsById(id);
    }

}
