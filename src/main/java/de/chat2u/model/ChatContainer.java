package de.chat2u.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created ChatContainer in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public class ChatContainer implements Iterable<Chat> {
    private Map<String, Chat> chats = new HashMap<>();

    public ChatContainer() {
        chats.put("global", new Chat());
    }

    public Chat getGlobalChat() {
        return getChat("global");
    }

    public String createNewChat(Chat chat) {
        if (!chats.containsKey(String.valueOf(chat.hashCode())))
            chats.put(String.valueOf(chat.hashCode()), chat);
        return String.valueOf(chat.hashCode());
    }

    public Chat getChat(String chatID) {
        return chats.get(chatID);
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
}
