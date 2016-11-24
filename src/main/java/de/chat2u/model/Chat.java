package de.chat2u.model;

import de.chat2u.authentication.UserRepository;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created Chat in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public class Chat implements Iterable<User> {
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
     * @return eine Liste aller Benutzer
     * */
    public UserRepository<User> getUsers() {
        return users;
    }

    /**
     * @return den HashCode des Chats als {@link String}
     * */
    public String getID() {
        return String.valueOf(hashCode());
    }

    @Override
    public Iterator<User> iterator() {
        return users.iterator();
    }

    @Override
    public void forEach(Consumer<? super User> action) {
        users.forEach(action);
    }

    @Override
    public Spliterator<User> spliterator() {
        return users.spliterator();
    }
}
