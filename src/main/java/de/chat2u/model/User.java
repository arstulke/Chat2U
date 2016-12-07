package de.chat2u.model;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created User in de.chat2u.model
 * by ARSTULKE on 17.11.2016.
 */
public class User {
    private final String username;
    private final List<Message> history;
    private Session session;

    User(String username) {
        this.history = new ArrayList<>();
        this.username = username;
    }

    User(String username, List<Message> history, Session session) {
        this.username = username;
        this.history = history;
        this.session = session;
    }

    /**
     * @return den Benutzernamen
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return den Chatverlauf aus {@link Message Message Objekten}
     */
    public List<Message> getHistory() {
        return history;
    }

    /**
     * @return die aktuelle WebSocketSession
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session setzt die aktuelle WebSocketSession
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * @param msg fügt eine Nachricht zum Chatverlauf hinzu
     */
    public void addMessageToHistory(Message msg) {
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
