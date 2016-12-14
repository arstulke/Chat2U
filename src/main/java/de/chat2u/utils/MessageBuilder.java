package de.chat2u.utils;

import de.chat2u.ChatServer;
import de.chat2u.model.Message;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static j2html.TagCreator.*;

/**
 * Created MessageBuilder in de.chat2u
 * by ARSTULKE on 18.11.2016.
 */
public class MessageBuilder {

    private final static Logger LOGGER = Logger.getLogger(MessageBuilder.class);

    public static JSONObject buildMessage(String type, Object primeData, Object secondData) {
        JSONObject output = new JSONObject();
        try {
            output.put("type", type);
            if (primeData != null)
                output.put("primeData", primeData);
            if (secondData != null)
                output.put("secondData", secondData);
            return output;
        } catch (JSONException e) {
            LOGGER.debug(e);
            return null;
        }
    }

    /**
     * <p>
     *
     * @param msg textMessage
     */
    public static JSONObject buildTextMessage(Message msg) {
        return buildMessage("textMessage", msg.getPrimeData(), getUserList());
    }

    /**
     * Creates a message in HTMl Format.
     *
     * @param sender    sender's username
     * @param message   text message
     * @param timestamp timestamp when the message where send
     */
    public static String createHTMLMessage(String sender, String message, String timestamp) {
        return article()
                .with(
                        b(sender),
                        unsafeHtml(" " + message),
                        p().with(small(timestamp).withClass("text-muted")))
                .render();
    }

    /**
     * @param date ist das Datum bzw. die Uhrzeit
     * @return Zeittempel
     */
    public static String getTimestamp(Date date) {
        return new SimpleDateFormat("HH:mm dd.MM.yyyy").format(date);
    }

    public static Object getUserList() {
        JSONArray users = new JSONArray();
        ChatServer.getOnlineUsers().forEach(user -> {
            JSONObject u = new JSONObject();
            try {
                u.put("name", user.getUsername());
                u.put("id", user.getUsername().hashCode());
            } catch (JSONException e) {
                LOGGER.debug(e);
            }
            users.put(u);
        });

        return users;
    }
}
