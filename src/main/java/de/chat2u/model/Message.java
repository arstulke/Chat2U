package de.chat2u.model;

import com.sun.istack.internal.NotNull;
import de.chat2u.utils.MessageBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;

/**
 * Created Message in de.chat2u.model
 * by ARSTULKE on 22.11.2016.
 */
@Entity
@Table(name = "message")
public class Message implements Comparable<Message> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "sender")
    private String sender;
    @Column(name = "text")
    private String message;
    @Column(name = "chat_id")
    private String chatID;
    @Column(name = "timestamp")
    private Date timestamp;

    public Message() {
    }

    public Message(String sender, String message, String chatID) {
        this(sender, message, chatID, new Date());
    }

    public Message(String sender, String message, String chatID, Date timestamp) {
        this.sender = sender;
        this.message = message;
        this.chatID = chatID;
        this.timestamp = timestamp;
    }

    /**
     * @return ein {@link JSONObject JSON Objekt} mit dem Zeitstempel, dem Absender, der ChatID und der Text-Nachricht
     */
    public JSONObject getPrimeData() {
        try {
            return new JSONObject()
                    .put("chatID", chatID)
                    .put("message", MessageBuilder.createHTMLMessage(sender, message, MessageBuilder.getTimestamp(timestamp)));
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", chatID='" + chatID + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int compareTo(Message o) {
        return timestamp.compareTo(o.timestamp);
    }

    public long getId() {
        return id;
    }

    public String getChatID() {
        return chatID;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
