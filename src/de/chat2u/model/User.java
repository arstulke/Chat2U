package de.chat2u.model;

import de.chat2u.authentication.Permissions;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created User in de.chat2u.model
 * by ARSTULKE on 17.11.2016.
 */
public class User {
    private final String username;
    private Permissions permissions;
    private List<String> history;
    private Session session;

    public User(String username, Permissions permissions) {
        this.history = new ArrayList<>();
        this.username = username;
        this.permissions = permissions;
    }

    public User(String username, Permissions permissions, List<String> history, Session session) {
        this.username = username;
        this.permissions = permissions;
        this.history = history;
        this.session = session;
    }

    /**
     * @return den Benutzernamen
     * */
    public String getUsername() {
        return username;
    }

    /**
     * @return das Berechtigungslevel
     * */
    public Permissions getPermissions() {
        return permissions;
    }

    /**
     * @return den Chatverlauf
     * */
    public List<String> getHistory() {
        return history;
    }

    /**
     * @return die aktuelle WebSocketSession
     * */
    public Session getSession() {
        return session;
    }

    /**
     * @param session setzt die aktuelle WebSocketSession
     * */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * @param msg fügt eine Nachricht zum Chatverlauf hinzu
     * */
    public void addMessageToHistory(String msg) {
        history.add(msg);
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
}
