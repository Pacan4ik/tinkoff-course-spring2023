package edu.java.scrapper.domain.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public interface LinkInfoAdapter {
    List<LinkInfoDto> findAllCheckedAtBefore(OffsetDateTime offsetDateTime);

    void updateCheckedAt(Long id, OffsetDateTime offsetDateTime);

    Collection<Long> getSubscribedChats(Long id);

    void updateAdditionalInfo(Long id, LinkInfoDto.AdditionalInfo additionalInfo);

    default JsonNode mergeJsonNodes(JsonNode oldNode, JsonNode newNode) {
        JsonNode finalNode = oldNode.deepCopy();
        newNode.fields().forEachRemaining(
            entry -> {
                String field = entry.getKey();
                JsonNode value = entry.getValue();
                ((ObjectNode) finalNode).set(field, value);
            }
        );
        return finalNode;
    }
}
