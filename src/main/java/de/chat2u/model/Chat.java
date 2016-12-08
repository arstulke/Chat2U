package de.chat2u.model;

import de.chat2u.ChatServer;
import de.chat2u.authentication.UserRepository;

import java.util.Date;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created Chat in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public class Chat implements Iterable<User> {
    private final UserRepository<User> users;
    private boolean global = false;
    private String id;

    public Chat(User... users){
        this.users = new UserRepository<>();
        this.global = false;
        for(User user : users){
            this.users.addUser(convertUser(user));
        }
    }

    Chat(UserRepository<User> users, boolean global) {
        this.users = new UserRepository<>();
        for(User user : users){
            this.users.addUser(convertUser(user));
        }
        this.global = global;
        this.id = setID(global);
    }

    private User convertUser(User user) {
        if(user instanceof AuthenticationUser){
            user = ((AuthenticationUser) user).getSimpleUser();
        }
        return user;
    }

    Chat(boolean global) {
        this(new UserRepository<>(), global);
    }

    /**
     * Fügt einen Benutzer zu einem Chat hinzu
     * <p>
     *
     * @param user ist der Benutzer der zum Chat hinzugefügt werden soll
     */
    public void addUser(User user) {
        users.addUser(user);
        this.id = setID(global);
    }

    /**
     * Entfernt einen Benutzer aus dem Chat (z.B. wenn dieser sich ausloggt)
     * <p>
     *
     * @param user ist der zu entfernende Benutzer
     */
    public void removeUser(User user) {
        users.removeUser(user);
        this.id = setID(global);
    }

    private String setID(boolean global) {
        return global ? ChatServer.GLOBAL : (String.valueOf(hashCode()) + "_" + new Date().getTime());
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
     */
    public int size() {
        return users.size();
    }

    /**
     * @return eine Liste aller Benutzer
     */
    public UserRepository<User> getUsers() {
        return users;
    }

    /**
     * @return den HashCode des Chats als {@link String}
     */
    public String getID() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat users1 = (Chat) o;

        return users != null ? users.equals(users1.users) : users1.users == null;

    }

    @Override
    public int hashCode() {
        return users != null ? users.hashCode() : 0;
    }
}
