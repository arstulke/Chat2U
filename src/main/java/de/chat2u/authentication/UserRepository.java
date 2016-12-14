package de.chat2u.authentication;

import de.chat2u.model.users.OnlineUser;
import de.chat2u.model.users.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created UserRepository in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class UserRepository<U extends User> implements Iterable<U> {
    private final Map<String, U> users = new ConcurrentHashMap<>();

    /**
     * Fügt einen {@link User} zur Liste hinzu.
     * <p>
     *
     * @param user ist der hinzu zufügende User.
     */
    public synchronized void addUser(U user) {
        users.put(user.getUsername(), user);
    }

    /**
     * Gibt einen User aus der Liste zurück, anhand des Benutzernames.
     * <p>
     *
     * @param username ist der Benutzername, des Benutzers
     * @return einen User, wenn dieser gefunden wurde oder {@code null} wenn
     * dieser nicht gefunden werden konnte
     */
    public synchronized U getByUsername(String username) {
        return users.get(username);
    }

    /**
     * Gibt alle Benutzernamen aus der Liste zurück.
     * <p>
     *
     * @return alle Benutzername der Liste
     */
    public synchronized Set<String> getUsernameList() {
        return users.keySet();
    }

    /**
     * Entfernt einen user aus der Liste
     * <p>
     *
     * @param user ist der zu entfernende User
     */
    public synchronized void removeUser(U user) {
        if (user != null && users.containsValue(user)) {
            users.remove(user.getUsername());
        }
    }

    /**
     * Überprüft ob ein Benutzername in der Liste enthalten ist
     * <p>
     *
     * @param username ist der zu überprüfende Benutzername
     */
    public synchronized boolean containsUsername(String username) {
        return users.containsKey(username);
    }

    public OnlineUser getBySession(Session webSocketSession) {
        for (User user : users.values()) {
            if (user instanceof OnlineUser) {
                OnlineUser onlineUser = (OnlineUser) user;
                if (onlineUser.getSession().equals(webSocketSession))
                    return onlineUser;
            }
        }
        return null;
    }

    @Override
    public synchronized Iterator<U> iterator() {
        return users.values().iterator();
    }

    @Override
    public synchronized void forEach(Consumer<? super U> action) {
        users.values().forEach(action);
    }

    @Override
    public synchronized Spliterator<U> spliterator() {
        return users.values().spliterator();
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRepository<?> that = (UserRepository<?>) o;

        return users.equals(that.users);

    }

    @Override
    public synchronized int hashCode() {
        return users.keySet().hashCode();
    }

    public Collection<User> toCollection() {
        Collection<User> users = new ArrayList<>();
        this.users.values().forEach(users::add);
        return users;
    }

    public U get(User user) {
        return users.get(user.getUsername());
    }
}
