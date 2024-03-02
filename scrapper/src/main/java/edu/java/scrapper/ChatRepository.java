package edu.java.scrapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
//TODO DELETE
public class ChatRepository {
    Map<Long, List<URI>> map = new ConcurrentHashMap<>();

    public boolean addUser(long id) {
        if (map.containsKey(id)) {
            return false;
        }
        map.put(id, new ArrayList<>());
        return true;
    }

    public boolean deleteUser(long id) {
        if (!map.containsKey(id)) {
            return false;
        }
        map.remove(id);
        return true;
    }

    public List<URI> getUserLinks(long id) {
        return map.get(id);
    }

    public boolean addLink(long id, URI link) {
        if (!map.containsKey(id)) {
            return false;
        }
        var list = map.get(id);
        return list.add(link);
    }

    public boolean deleteLink(long id, URI link) {
        List<URI> list = map.get(id);
        if (list == null) {
            return false;
        }
        return list.remove(link);
    }

    public boolean checkUser(long id) {
        return map.containsKey(id);
    }
}
