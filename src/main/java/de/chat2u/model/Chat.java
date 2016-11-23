package de.chat2u.model;

import de.chat2u.ChatServer;
import de.chat2u.authentication.UserRepository;
import de.chat2u.utils.MessageBuilder;

import java.io.IOException;

/**
 * Created Chat in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public class Chat {
    private UserRepository<User> users;

    public Chat(User... users) {
        this();
        for (User user : users) {
            this.users.addUser(user);
        }
    }

    public Chat(UserRepository<User> users) {
        this.users = users;
    }

    public Chat() {
        users = new UserRepository<>();
    }

    public void sendMessage(Message msg) {
        String message = MessageBuilder.buildMessage(msg);
        users.forEach(user -> {
            try {
                user.addMessageToHistory(msg);
                ChatServer.sendMessageToSession(message, user.getSession());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void addUser(User user) {
        users.addUser(user);
    }

    public boolean contains(User user) {
        return users.contains(user);
    }

    public void removeUser(User user) {
        users.removeUser(user);
    }
}
