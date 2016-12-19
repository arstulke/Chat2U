package de.chat2u.model.chats;

import de.chat2u.ChatServer;
import de.chat2u.model.users.User;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Created Chat in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
public class Chat implements Iterable<User> {

    final Set<String> userList;
    String id;
    final String name;

    Chat(String name, Set<String> userList) {
        this.name = name;
        this.userList = userList;
        this.id = String.valueOf(hashCode());
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

        return userList.equals(users1.userList) && name.equals(users1.name);
    }

    @Override
    public int hashCode() {
        int result = userList.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public Iterator<User> iterator() {
        return stream().iterator();
    }

    public Stream<User> stream() {
        return ChatServer.getOnlineUsers().stream().filter(new Predicate<User>() {
            @Override
            public boolean test(User user) {
                return contains(userList, user);
            }

            private boolean contains(Set<String> users, User user) {
                for (String u : users)
                    if (u.equals(user.getUsername())) {
                        return true;
                    }
                return false;
            }
        });
    }

    @Override
    public Spliterator<User> spliterator() {
        return stream().spliterator();
    }

    public boolean contains(String username) {
        return userList.contains(username);
    }

    public Set<String> getUsers() {
        return userList;
    }
}
