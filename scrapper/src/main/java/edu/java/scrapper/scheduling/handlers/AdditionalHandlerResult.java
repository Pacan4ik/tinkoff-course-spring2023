package edu.java.scrapper.scheduling.handlers;

import edu.java.scrapper.domain.adapters.LinkInfoDto;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AdditionalHandlerResult {
    private final List<String> descriptions;

    @Setter
    private Consumer<LinkInfoDto.AdditionalInfo> additionalInfoConsumer;

    public AdditionalHandlerResult() {
        this.descriptions = new ArrayList<>();
        this.additionalInfoConsumer = (additionalInfo) -> {
        };
    }
}
