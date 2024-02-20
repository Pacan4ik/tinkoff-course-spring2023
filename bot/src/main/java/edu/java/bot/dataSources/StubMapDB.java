package edu.java.bot.dataSources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class StubMapDB implements UsersTracksDB {
    Map<Long, List<String>> map = new ConcurrentHashMap<>();

    @Override
    public List<String> getUserLinks(long id) {
        return map.get(id);
    }

    @Override
    public boolean addLink(long id, String link) {
        List<String> list = map.computeIfAbsent(id, k -> new ArrayList<>());
        return list.add(link);
    }

    @Override
    public boolean deleteLink(long id, String link) {
        List<String> list = map.get(id);
        if (list == null) {
            return false;
        }
        return list.remove(link);
    }
}
