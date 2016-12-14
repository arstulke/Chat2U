package de.chat2u.model.chats;

import de.chat2u.model.Message;
import de.chat2u.model.users.User;

import java.util.*;

/**
 * Created Group in de.chat2u
 * by ARSTULKE on 14.12.2016.
 */
public class Group extends Chat {

    private final List<Message> history;

    private Group(String name, List<Message> history, Set<User> users) {
        super(name, users);
        this.history = history;
        super.id = String.valueOf(hashCode());
    }

    public Group(String name, List<Message> history) {
        this(name, history, new HashSet<>());
    }

    public Group(String name, Set<User> users) {
        this(name, new ArrayList<>(), users);
    }

    public Group(String name) {
        this(name, new HashSet<>());
    }

    public List<Message> getHistory() {
        return history;
    }
}
