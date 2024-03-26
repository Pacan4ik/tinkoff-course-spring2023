package edu.java.scrapper.scheduling.handlers;

import edu.java.scrapper.domain.jdbc.dao.LinkRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AdditionalHandlerResult {
    private final List<String> descriptions;

    @Setter
    private Consumer<LinkRepository> rowUpdateConsumer;

    public AdditionalHandlerResult() {
        this.descriptions = new ArrayList<>();
        this.rowUpdateConsumer = (linkRepository) -> {
        };
    }
}
