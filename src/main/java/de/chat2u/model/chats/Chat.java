package de.chat2u.model.chats;

import de.chat2u.ChatServer;
import de.chat2u.model.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created Chat in de.chat2u.model
 * by ARSTULKE on 23.11.2016.
 */
@Entity
@Table(name = "chat")
@DiscriminatorColumn(name = "type")
public class Chat implements Iterable<User> {

    @Id
    @Column(name = "id")
    String id;

    @ManyToMany(targetEntity = User.class, cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name="chat_user",
            joinColumns=@JoinColumn(name="chat_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="username", referencedColumnName="name"))
    private Set<User> userList;

    @Column(name = "name")
    String name;

    public Chat() {
    }

    Chat(String name, Set<String> users) {
        this.name = name;
        this.userList = new HashSet<>();
        users.forEach(username -> userList.add(ChatServer.userDataBase.getByUsername(username)));

        this.id = String.valueOf(hashCode());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<User> getUserList() {
        return userList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }

    public void setName(String name) {
        this.name = name;
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

            private boolean contains(Set<User> users, User user) {
                for (User u : users)
                    if (u.getUsername().equals(user.getUsername())) {
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
        return userList.contains(ChatServer.userDataBase.getByUsername(username));
    }

    public Set<String> getUsers() {
        return userList.stream().collect(
                HashSet::new,
                (strings, user) -> strings.add(user.getUsername()),
                (BiConsumer<Set<String>, Set<String>>) Set::addAll);
    }
}
