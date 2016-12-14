package de.chat2u.model.chats;

import de.chat2u.model.users.User;

import java.util.HashSet;

/**
 * Created Channel in de.chat2u.model
 * by ARSTULKE on 14.12.2016.
 */
public class Channel extends Chat {

    public Channel(String name, String chatID) {
        super(name, chatID, new HashSet<>());
    }

    @Override
    public int hashCode(){
        return super.name.hashCode();
    }

    public void addUser(User user) {
        super.users.add(user);
    }

    public void removeUser(User user) {
        super.users.remove(user);
    }
}