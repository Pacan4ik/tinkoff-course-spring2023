package edu.java.scrapper.domain.adapters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.domain.jpa.dao.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkInfoAdapter implements LinkInfoAdapter {
    private final LinkRepository linkRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<LinkInfoDto> findAllCheckedAtBefore(OffsetDateTime offsetDateTime) {
        return linkRepository.findByCheckedAtLessThan(offsetDateTime).stream()
            .map(link -> new LinkInfoDto(
                link.getId(),
                URI.create(link.getUrl()),
                link.getCheckedAt(),
                objectMapper.convertValue(link.getAdditionalInfo(), LinkInfoDto.AdditionalInfo.class)
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

    @Override
    public void updateAdditionalInfo(Long id, LinkInfoDto.AdditionalInfo additionalInfo) {
        linkRepository.updateAdditionalInfoById(
            objectMapper.convertValue(
                additionalInfo,
                new TypeReference<Map<String, Object>>() {
                }
            ), id);
    }
}
