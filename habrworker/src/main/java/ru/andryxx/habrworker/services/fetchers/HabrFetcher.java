package ru.andryxx.habrworker.services.fetchers;

import ru.andryxx.habrworker.services.fetchers.model.HabrDTO;
import java.net.URI;

public interface HabrFetcher {
    HabrDTO fetch(URI url);
}
