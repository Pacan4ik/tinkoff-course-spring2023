package edu.java.bot.api.services;

import java.net.URI;
import java.util.List;

public interface UpdateHandlerService {
    void handleUpdate(URI url, String description, List<Long> ids);
}
