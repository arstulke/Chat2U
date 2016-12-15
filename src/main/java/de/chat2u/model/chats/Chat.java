package de.chat2u.model.chats;

import de.chat2u.ChatServer;
import de.chat2u.model.users.User;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created Chat in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public class Chat implements Iterable<User> {

    final Set<User> users;
    String id;
    final String name;

    Chat(String name, Set<User> users) {
        this.name = name;
        this.users = users;
        this.id = String.valueOf(hashCode());
    }

    public Chat(String name, String chatID, HashSet<User> users) {
        this.name = name;
        this.users = users;
        this.id = chatID;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
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

    @Override
    public Iterator<User> iterator() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void forEach(Consumer<? super User> action) {
        Set<User> onlineUsers = new HashSet<>();
        ChatServer.getOnlineUsers().toCollection().stream().filter(new Predicate<User>() {
            @Override
            public boolean test(User user) {
                return contains(users, user);
            }

            private boolean contains(Set<User> users, User user) {
                for (User u : users)
                    if (u.equals(user)) {
                        return true;
                    }
                return false;
            }
        }).forEach(onlineUsers::add);
        onlineUsers.forEach(action);
    }

    @Override
    public Spliterator<User> spliterator() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public Set<User> getUsers() {
        return users;
    }
}
