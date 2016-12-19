package de.chat2u.model.chats;

import de.chat2u.ChatServer;
import de.chat2u.model.Message;

import java.util.List;
import java.util.Set;

import static de.chat2u.ChatServer.chats;

/**
 * Created Group in de.chat2u
 * by ARSTULKE on 14.12.2016.
 */
public class Group extends Chat {

    private final List<Message> history;

    public Group(String name, List<Message> history, Set<String> userList) {
        super(name, userList);
        this.history = history;
        super.id = String.valueOf(hashCode());
    }

    public List<Message> getHistory() {
        return history;
    }

    public void addMessageToHistory(Message message) {
        List<Message> history = ChatServer.chats.addMessageToHistory(message);

        this.history.clear();
        this.history.addAll(history);
    }
}
