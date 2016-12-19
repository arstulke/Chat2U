package de.chat2u.model.users;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashSet;
import java.util.Set;

/**
 * Created User in de.chat2u.model
 * by ARSTULKE on 17.11.2016.
 */
public class User {
    private final String username;
    private final Set<String> groups;

    public User(String username) {
        this(username, new HashSet<>());
    }

    public User(String username, Set<String> groups) {
        this.username = username;
        this.groups = groups;
    }

    /**
     * @return den Benutzernamen
     */
    public String getUsername() {
        return username;
    }

    public Set<String> getGroups() {
        return groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", groups=" + groups +
                '}';
    }
}
