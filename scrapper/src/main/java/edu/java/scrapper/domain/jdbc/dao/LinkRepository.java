package edu.java.scrapper.domain.jdbc.dao;

import edu.java.scrapper.domain.jdbc.dto.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    LinkDto add(URI url);

    LinkDto add(URI url, String description);

    LinkDto remove(Long id);

    List<LinkDto> remove(Long... ids);

    List<LinkDto> findAll();

    List<LinkDto> findAll(URI... urls);

    Optional<LinkDto> find(URI url);

    List<LinkDto> findAll(Long... ids);

    Optional<LinkDto> find(Long id);

    List<LinkDto> findAllWhereCheckedAtBefore(OffsetDateTime offsetDateTime);

    LinkDto updateUpdatedAt(Long id, OffsetDateTime newOffsetDateTime);

    LinkDto updateCheckedAt(Long id, OffsetDateTime newOffsetDateTime);

    List<Long> getChats(Long linkId);

    LinkDto updateAdditionalInfo(Long id, String fieldName, Object value);

}
