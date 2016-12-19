package de.chat2u.cucumber.selenium;

import de.chat2u.ChatServer;
import de.chat2u.model.Message;
import de.chat2u.model.chats.Channel;
import de.chat2u.model.chats.Chat;
import de.chat2u.model.chats.Group;
import de.chat2u.model.users.User;
import de.chat2u.persistence.chats.ChatContainer;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Created ChatContainer in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public class OfflineChatContainer implements ChatContainer {
    private final Map<String, Chat> chats = new HashMap<>();

    /**
     * @see ChatServer#createGroup(String, Set)
     */
    public String createGroup(String name, Set<User> users) {
        Set<String> userList = users.stream()
                .collect(
                        HashSet::new,
                        (strings, user) -> strings.add(user.getUsername()),
                        (BiConsumer<Set<String>, Set<String>>) Set::addAll);

        Group groupChat = new Group(name, new ArrayList<>(), userList);
        if (!chats.containsKey(groupChat.getID())) {
            chats.put(groupChat.getID(), groupChat);
            return groupChat.getID();
        }

        return null;
    }

    public void createNewChannel(String name) {
        Chat chat = new Channel(name);
        if (!chats.containsKey(chat.getID())) {
            chats.put(chat.getID(), chat);
        }
    }

    public Chat getChatByID(String id) {
        return chats.get(id);
    }

    public Set<Channel> getChannels() {
        return chats.values().stream()
                .filter(chat -> chat instanceof Channel)
                .collect(HashSet::new, (BiConsumer<Set<Channel>, Chat>) (channels, users) -> channels.add((Channel) users), Set::addAll);
    }

    @Override
    public void addUserToChat(String id, String username) {
        ((Channel) chats.get(id)).addUser(username);
    }

    @Override
    public void removeUserFromChat(String id, String username) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public List<Message> addMessageToHistory(Message message) {
        ((Group) getChatByID(message.getChatID())).getHistory().add(message);
        return ((Group) getChatByID(message.getChatID())).getHistory();
    }

    @Override
    public void close() throws Exception {
    }
}
