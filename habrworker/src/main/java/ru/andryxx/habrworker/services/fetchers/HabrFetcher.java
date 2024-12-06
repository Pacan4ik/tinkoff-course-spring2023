package ru.andryxx.habrworker.services.fetchers;

import java.net.URI;
import ru.andryxx.habrworker.services.fetchers.model.HabrDTO;

public interface HabrFetcher {
    HabrDTO fetch(URI url);
}
