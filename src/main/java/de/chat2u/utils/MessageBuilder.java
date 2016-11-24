package de.chat2u.utils;

import de.chat2u.ChatServer;
import de.chat2u.model.Message;
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
    /**
     * Generieren der zu sendenen Nachricht. Die Nachricht wird zusammengesetzt aus
     * <p>- Absender
     * <p>- Nachricht
     * <p>
     *
     * @param msg ist die zu sendene Nachricht
     * @param type is the message type (e.g.: msg, server_msg)
     * @return die fertig generierte Nachricht.
     */
    public static JSONObject buildMessage(Message msg, String type) {
        try {
            return msg.getJSON()
                    .put("type", type)
                    .put("userlist", ChatServer.getOnlineUsers().getUsernameList());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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
     * Baut aus eine nachricht aus einer Exception.
     * <p>
     *
     * @param exception ist die Exception, zu der eine Nachricht gebaut werden soll.
     * @return die gebaute Nachricht
     */
    public static String buildExceptionMessage(Exception exception) {
        try {
            String timestamp = getTimestamp(new Date());
            return String.valueOf(new JSONObject()
                    .put("type", "error")
                    .put("exceptionType", exception.getClass().getSimpleName())
                    .put("exceptionMessage", exception.getMessage())
                    .put("timestamp", timestamp)
                    .put("msg", "<p style=\"color:#F70505\">" + exception.getMessage() + "</p>"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param date ist das Datum bzw. die Uhrzeit
     * @return Zeittempel
     */
    public static String getTimestamp(Date date) {
        return new SimpleDateFormat("HH:mm dd.MM.yyyy").format(date);
    }
}
