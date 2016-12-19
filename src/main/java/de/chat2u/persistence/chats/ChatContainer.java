package de.chat2u.persistence.chats;

import de.chat2u.ChatServer;
import de.chat2u.model.Message;
import de.chat2u.model.chats.Channel;
import de.chat2u.model.chats.Chat;
import de.chat2u.model.users.User;

import java.util.List;
import java.util.Set;

/**
 * Created ChatContainer in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public interface ChatContainer extends AutoCloseable {
    /**
     * @see ChatServer#createGroup(String, Set)
     */
    String createGroup(String name, Set<User> users);

    void createNewChannel(String name);

    Chat getChatByID(String id);

    Set<Channel> getChannels();

    void addUserToChat(String id, String username);

    void removeUserFromChat(String id, String username);

    List<Message> addMessageToHistory(Message message);
}
