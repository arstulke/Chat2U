package de.chat2u.persistence;

import de.chat2u.ChatServer;
import de.chat2u.model.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created OnlineUserList in de.chat2u
 * by ARSTULKE on 15.12.2016.
 */
public class OnlineUserList implements Iterable<User> {
    private final Map<String, Session> users = new ConcurrentHashMap<>();

    /**
     * Entfernt einen user aus der Liste
     * <p>
     *
     * @param username ist der Username des zu entfernende Benutzers
     */
    public void removeUser(String username) {
        if (username != null && users.containsKey(username)) {
            users.remove(username);
        }
    }

    public String getUsernameBySession(Session webSocketSession) {
        for (Map.Entry<String, Session> entry : users.entrySet()) {
            if (entry.getValue().equals(webSocketSession))
                return entry.getKey();
        }
        return null;
    }

    /**
     * Gibt alle Benutzernamen aus der Liste zurück.
     * <p>
     *
     * @return alle Benutzername der Liste
     */
    public Set<String> getUsernameList() {
        return users.keySet();
    }

    public Session getSessionByName(String username) {
        return users.get(username);
    }

    public boolean contains(String username) {
        return users.containsKey(username);
    }

    public void addUser(String username, Session userSession) {
        users.put(username, userSession);
    }

    public Stream<User> stream() {
        Set<User> userSet = new HashSet<>();
        users.keySet().forEach(username -> userSet.add(ChatServer.userDataBase.getByUsername(username)));
        return userSet.stream();
    }

    @Override
    public Iterator<User> iterator() {
        return stream().iterator();
    }

    @Override
    public void forEach(Consumer<? super User> action) {
        stream().forEach(action);
    }

    @Override
    public Spliterator<User> spliterator() {
        return stream().spliterator();
    }
}
