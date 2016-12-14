package de.chat2u.model.users;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Set;

/**
 * Created OnlineUser in de.chat2u.model
 * by ARSTULKE on 14.12.2016.
 */
public class OnlineUser extends User {
    private Session session;

    OnlineUser(String username) {
        super(username);
    }

    OnlineUser(String username, Set<String> groups) {
        super(username, groups);
    }

    public void setSession(Session session){
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}
