package edu.java.bot.dataSources;

import java.util.List;

public interface UsersTracksDB {
    List<String> getUserLinks(long id);

    boolean addLink(long id, String link);

    boolean deleteLink(long id, String link);
}
