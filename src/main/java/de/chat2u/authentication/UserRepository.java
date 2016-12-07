package de.chat2u.authentication;

import de.chat2u.model.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created UserRepository in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class UserRepository<U extends User> implements Iterable<U> {
    private final HashMap<String, U> users = new HashMap<>();

    /**
     * Fügt einen {@link User} zur Liste hinzu.
     * <p>
     *
     * @param user ist der hinzu zufügende User.
     */
    public void addUser(U user) {
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
    public U getByUsername(String username) {
        return users.get(username);
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

    /**
     * Entfernt einen user aus der Liste
     * <p>
     *
     * @param user ist der zu entfernende User
     */
    public void removeUser(U user) {
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
    public boolean containsUsername(String username) {
        return users.containsKey(username);
    }

    public User getBySession(Session webSocketSession) {
        for (User user : users.values()) {
            if (user.getSession().equals(webSocketSession))
                return user;
        }
        return null;
    }

    public boolean contains(U user) {
        return users.values().contains(user);
    }

    public int size() {
        return users.size();
    }

    @Override
    public Iterator<U> iterator() {
        return users.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super U> action) {
        users.values().forEach(action);
    }

    @Override
    public Spliterator<U> spliterator() {
        return users.values().spliterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRepository<?> that = (UserRepository<?>) o;

        return users != null ? users.equals(that.users) : that.users == null;

    }

    @Override
    public int hashCode() {
        return users != null ? users.keySet().hashCode() : 0;
    }
}
