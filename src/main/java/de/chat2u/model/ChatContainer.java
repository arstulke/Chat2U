package de.chat2u.model;

import de.chat2u.ChatServer;
import de.chat2u.authentication.UserRepository;

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
        chats.put(ChatServer.GLOBAL, new Chat(true));
    }

    /**
     * @see ChatServer#createChat(UserRepository)
     */
    public String createNewChat(UserRepository<User> users) {
        Chat chat = new Chat(users, false);
        if (!chats.containsKey(String.valueOf(chat.hashCode()))) {
            chats.put(String.valueOf(chat.hashCode()), chat);
            return String.valueOf(chat.hashCode());
        }
        return null;
    }

    /**
     * Überschreibt einen Chat
     * <p>
     *
     * @param chatID ist die zu überschreibende ChatID
     * @param users  ist die neue benutzerliste des Chats
     * @param global zeigt an ob der Chat der Globale Chat ist
     */
    public void overwrite(String chatID, UserRepository<User> users, boolean global) {
        chats.put(chatID, new Chat(users ,global));
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
