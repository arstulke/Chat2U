package de.chat2u.model;

import de.chat2u.authentication.UserRepository;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created Chat in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public class Chat implements Iterable<User> {
    private final UserRepository<User> users;
    private boolean permanent = false;
    private String id;
    private String name;

    Chat(Collection<User> users, String name, String chatID) {
        this.users = new UserRepository<>();
        //region set users
        for (User user : users) {
            this.users.addUser(convertUser(user));
        }
        //endregion
        this.permanent = !chatID.contains("_");
        this.name = name;
        this.id = chatID;
        setID();
    }

    Chat(String name, String chatID) {
        this(new ArrayList<>(), name, chatID);
    }

    private User convertUser(User user) {
        if (user instanceof AuthenticationUser) {
            user = ((AuthenticationUser) user).getSimpleUser();
        }
        return user;
    }

    /**
     * Fügt einen Benutzer zu einem Chat hinzu
     * <p>
     *
     * @param user ist der Benutzer der zum Chat hinzugefügt werden soll
     */
    public void addUser(User user) {
        users.addUser(user);
        setID();
    }

    /**
     * Entfernt einen Benutzer aus dem Chat (z.B. wenn dieser sich ausloggt)
     * <p>
     *
     * @param user ist der zu entfernende Benutzer
     */
    public void removeUser(User user) {
        users.removeUser(user);
        setID();
    }

    private void setID() {
        this.id = id.contains("_") ? (String.valueOf(hashCode()) + "_" + new Date().getTime()) : id;
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

    public String getName() {
        return name;
    }

    public boolean isPermanent() {
        return permanent;
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

        return users.equals(users1.users) && name.equals(users1.name);

    }

    @Override
    public int hashCode() {
        int result = users.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
