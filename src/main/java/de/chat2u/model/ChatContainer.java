package de.chat2u.model;

import de.chat2u.ChatServer;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created ChatContainer in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public class ChatContainer implements Iterable<Chat> {
    private final Map<String, Chat> chats = new HashMap<>();

    public ChatContainer() {
        chats.put(ChatServer.GLOBAL, new Chat("Global", ChatServer.GLOBAL));
    }

    /**
     * @see ChatServer#createChat(Collection, String)
     */
    public String createNewChat(Collection<User> users, String name) {
        Chat chat = new Chat(users, name, "_");
        if (!chats.containsKey(chat.getID())) {
            chats.put(chat.getID(), chat);
            return chat.getID();
        }
        return null;
    }

    /**
     * Überschreibt einen Chat
     * <p>
     *  @param chatID ist die zu überschreibende ChatID
     * @param users  ist die neue benutzerliste des Chats
     */
    public void overwrite(String chatID, Collection<User> users) {
        Chat chat = chats.get(chatID);
        chats.put(chatID, new Chat(users, chat.getName(), chatID));
    }

    /**
     * Entfernt einen Chat aus der Liste der Chats
     * <p>
     *
     * @param chatID ist die ID des zu löschenden Chats
     */
    public void removeChat(String chatID) {
        chats.remove(chatID);
    }

    /**
     * Gibt den Chat zur gegebenen ChatID zurück.
     * <p>
     *
     * @param chatID ist die ID des zu suchenden Chats
     *               <p>
     * @return das Chat Objekt zur dazugehörigen ChatID
     * <p>
     */
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
