package de.chat2u.cucumber.selenium;

import de.chat2u.ChatServer;
import de.chat2u.model.Message;
import de.chat2u.model.User;
import de.chat2u.model.chats.Channel;
import de.chat2u.model.chats.Chat;
import de.chat2u.model.chats.Group;
import de.chat2u.persistence.chats.ChatContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

        Group groupChat = new Group(name, userList);
        if (!chats.containsKey(groupChat.getId())) {
            chats.put(groupChat.getId(), groupChat);
            return groupChat.getId();
        }

        return null;
    }

    public void createNewChannel(String name) {
        Chat chat = new Channel(name);
        if (!chats.containsKey(chat.getId())) {
            chats.put(chat.getId(), chat);
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
    public Chat addUserToChat(String id, String username) {
        Chat chat = ChatServer.chats.getChatByID(id);
        chat.getUserList().add(ChatServer.userDataBase.getByUsername(username));

        return getChatByID(id);
    }

    @Override
    public void removeUserFromChat(String id, String username) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void addMessageToHistory(Message message) {
        Chat chat = getChatByID(message.getChatID());
        if (chat instanceof Group)
            ((Group) chat).addMessageToHistory(message);
    }

    @Override
    public Set<Group> getGroupsFrom(String username) {
        Set<Group> groups = new HashSet<>();
        chats.values().forEach(chat -> {
            if (chat instanceof Group && chat.contains(username)) {
                groups.add((Group) chat);
            }
        });

        return groups;
    }

    @Override
    public void close() throws Exception {
    }
}
