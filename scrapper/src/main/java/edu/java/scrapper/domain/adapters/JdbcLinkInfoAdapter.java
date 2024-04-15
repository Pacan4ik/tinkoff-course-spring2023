package edu.java.scrapper.domain.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.domain.jdbc.dao.ChatRepository;
import edu.java.scrapper.domain.jdbc.dao.LinkRepository;
import edu.java.scrapper.domain.jdbc.dto.ChatDto;
import edu.java.scrapper.domain.jdbc.dto.LinkDto;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcLinkInfoAdapter implements LinkInfoAdapter {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<LinkInfoDto> findAllCheckedAtBefore(OffsetDateTime offsetDateTime) {
        return linkRepository.findAllWhereCheckedAtBefore(offsetDateTime).stream()
            .map(linkDto -> new LinkInfoDto(
                linkDto.id(),
                linkDto.url(),
                linkDto.checkedAt(),
                objectMapper.convertValue(linkDto.additionalInfo(), LinkInfoDto.AdditionalInfo.class)
            ))
            .toList();
    }

    @Override
    public void updateCheckedAt(Long id, OffsetDateTime offsetDateTime) {
        linkRepository.updateCheckedAt(id, offsetDateTime);
    }

    @Override
    public Collection<Long> getSubscribedChats(Long id) {
        return chatRepository.getAllChats(id).stream()
            .map(ChatDto::id)
            .toList();
    }

    @Override
    @Transactional
    public void updateAdditionalInfo(Long id, LinkInfoDto.AdditionalInfo additionalInfo) {
        Optional<LinkDto> linkDto = linkRepository.find(id);
        JsonNode oldNode = objectMapper.createObjectNode();
        if (linkDto.isPresent()) {
            oldNode = linkDto.get().additionalInfo();
        }

        JsonNode newNode = objectMapper.convertValue(additionalInfo, JsonNode.class);

        JsonNode finalNode = mergeJsonNodes(oldNode, newNode);
        linkRepository.setAdditionalInfo(id, finalNode.toString());
    }
}
