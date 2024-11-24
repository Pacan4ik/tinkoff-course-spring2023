package edu.java.scrapper.domain.adapters;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LinkDto {
    private Long id;
    private URI url;
    private OffsetDateTime checkedAt;
}
