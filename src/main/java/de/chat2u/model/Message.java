package de.chat2u.model;

import de.chat2u.utils.MessageBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created Message in de.chat2u.model
 * by ARSTULKE on 22.11.2016.
 */
public class Message {
    private final String sender;
    private final String message;
    private final String chatID;
    private final Date timestamp;

    public Message(String sender, String message, String chatID, Date timestamp) {
        this.sender = sender;
        this.message = message;
        this.chatID = chatID;
        this.timestamp = timestamp;
    }

    public Message(String sender, String message, String chatID) {
        this.sender = sender;
        this.message = message;
        this.chatID = chatID;
        this.timestamp = new Date();
    }

    /**
     * @return ein {@link JSONObject JSON Objekt} mit dem Zeitstempel, dem Absender, der ChatID und der Text-Nachricht
     * */
    public JSONObject getJSON() throws JSONException {
        return new JSONObject()
                .put("type", "msg")
                .put("sender", sender)
                .put("timestamp", MessageBuilder.getTimestamp(timestamp))
                .put("chatID", chatID)
                .put("userMessage", MessageBuilder.createHTMLMessage(sender, message, MessageBuilder.getTimestamp(timestamp)))
                .put("noHTMLmsg", message);
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
}
