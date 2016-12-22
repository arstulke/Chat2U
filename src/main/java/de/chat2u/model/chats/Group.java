package de.chat2u.model.chats;

import de.chat2u.model.Message;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created Group in de.chat2u
 * by ARSTULKE on 14.12.2016.
 */
@Entity
@DiscriminatorValue(value = "gr")
public class Group extends Chat {

    @OneToMany(targetEntity = Message.class, cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private Set<Message> history;

    public Group() {
    }

    public Group(String name, Set<String> userList) {
        super(name, userList);
        this.history = new HashSet<>();
        super.id = String.valueOf(hashCode());
    }

    public List<Message> getHistory() {
        List<Message> history = new ArrayList<>(this.history);
        history.sort(Message::compareTo);
        return history;
    }

    public void setHistory(List<Message> history) {
        this.history = new HashSet<>(history);
    }

    public void addMessageToHistory(Message msg){
        history.add(msg);
    }
}
