package de.chat2u.model.chats;

import de.chat2u.ChatServer;
import de.chat2u.model.users.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created Channel in de.chat2u.model
 * by ARSTULKE on 14.12.2016.
 */
public class Channel extends Chat {

    public Channel(String name) {
        this(name, new HashSet<>());
    }

    public Channel(String name, Set<String> users) {
        super(name, users);
        if(!String.valueOf(hashCode()).equals(ChatServer.LobbyID))
            super.id = "c_" + hashCode();
        else
            super.id = String.valueOf(hashCode());
    }



    @Override
    public int hashCode(){
        return super.name.hashCode();
    }

    public void addUser(String username) {
        super.userList.add(username);
        ChatServer.chats.addUserToChat(this.getID(), username);
    }

    public void addUser(User user) {
        addUser(user.getUsername());

        User secUser = ChatServer.userDataBase.getByUsername(user.getUsername());
        user.getGroups().clear();
        user.getGroups().addAll(secUser.getGroups());
    }

    public void removeUser(String username) {
        super.userList.remove(username);
        ChatServer.chats.removeUserFromChat(this.getID(), username);
    }
}
