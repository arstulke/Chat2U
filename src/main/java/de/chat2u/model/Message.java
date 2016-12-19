package de.chat2u.model;

import com.sun.javafx.binding.SelectBinding;
import de.chat2u.utils.MessageBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created Message in de.chat2u.model
 * by ARSTULKE on 22.11.2016.
 */
public class Message implements Comparable<Message> {
    private final String sender;
    private final String message;
    private final String chatID;
    private final Date timestamp;

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
}
