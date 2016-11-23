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

    /**
     * Sendet eine {@link Message Nachricht} an alle User des Chats
     * <p>
     *
     * @param msg ist die zu sendende Nachricht
     */
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

    /**
     * Fügt einen Benutzer zu einem Chat hinzu
     * <p>
     *
     * @param user ist der Benutzer der zum Chat hinzugefügt werden soll
     */
    public void addUser(User user) {
        users.addUser(user);
    }

    /**
     * Entfernt einen Benutzer aus dem Chat (z.B. wenn dieser sich ausloggt)
     * <p>
     * @param user ist der zu entfernende Benutzer
     */
    public void removeUser(User user) {
        users.removeUser(user);
    }

    /**
     * @param user ist der zu suchende Benutzer
     *             <p>
     * @return {@code true} wenn der Benutzer an diesem Chat teilnimmt
     */
    public boolean contains(User user) {
        return users.contains(user);
    }

    /**
     * @return die Anzahl an Benutzern diese Chats
     * */
    public int size() {
        return users.size();
    }

    /**
     * @return eine Lite aller Benutzer
     * */
    public UserRepository<User> getUsers() {
        return users;
    }
}
