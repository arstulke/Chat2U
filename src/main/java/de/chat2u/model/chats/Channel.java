package de.chat2u.model.chats;

import de.chat2u.ChatServer;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

/**
 * Created Channel in de.chat2u.model
 * by ARSTULKE on 14.12.2016.
 */
@Entity
@DiscriminatorValue(value = "ch")
public class Channel extends Chat {

    public Channel() {
    }

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

}
