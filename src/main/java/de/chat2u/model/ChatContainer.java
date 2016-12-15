package de.chat2u.model;

import de.chat2u.ChatServer;
import de.chat2u.model.chats.Channel;
import de.chat2u.model.chats.Chat;
import de.chat2u.model.chats.Group;
import de.chat2u.model.users.User;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created ChatContainer in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public class ChatContainer implements Iterable<Chat> {
    private final Map<String, Chat> chats = new HashMap<>();

    /**
     * @see ChatServer#createGroup(String, Collection)
     */
    public String createGroup(String name, Collection<User> users) {
        Group groupChat = new Group(name, new HashSet<>(users));
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

    @Override
    public Iterator<Chat> iterator() {
        return chats.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super Chat> action) {
        chats.values().forEach(action);
    }

    @Override
    public Spliterator<Chat> spliterator() {
        return chats.values().spliterator();
    }

    public Stream<Chat> getChannels() {
        return chats.values().stream().filter(chat -> chat instanceof Channel);
    }
}
