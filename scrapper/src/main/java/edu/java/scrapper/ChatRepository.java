package edu.java.scrapper;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Repository;

@Repository
//TODO DELETE
public class ChatRepository {
    private final Map<Long, List<URI>> map = new ConcurrentHashMap<>();

    public boolean addUser(long id) {
        map.putIfAbsent(id, new CopyOnWriteArrayList<>());
        return true;
    }

    public boolean deleteUser(long id) {
        map.remove(id);
        return true;
    }

    public List<URI> getUserLinks(long id) {
        return Collections.unmodifiableList(map.get(id));
    }

    public boolean addLink(long id, URI link) {
        map.computeIfPresent(id, (key, list) -> {
                list.add(link);
                return list;
            }
        );
        return true;
    }

    public boolean deleteLink(long id, URI link) {
        map.computeIfPresent(id, (key, list) -> {
                list.add(link);
                return list;
            }
        );
        return true;
    }

    public boolean checkUser(long id) {
        return map.containsKey(id);
    }
}
