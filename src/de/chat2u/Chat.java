package de.chat2u;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;

/**
 * Created Chat in PACKAGE_NAME
 * by ARSTULKE on 16.11.2016.
 */
public class Chat {
    static Map<Session, User> users = new ConcurrentHashMap<>();

    /**
     * Broadcasts the message to all users, who are online.
     * <p>
     *
     * @param sender  is a normal {@link String} with sender's username
     * @param message is a normal {@link String} with text message
     */
    public static void broadcastMessage(String sender, String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());

        List<String> usernameList = Utils.getUsernameList();
        users.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("sender", sender)
                        .put("timestamp", timestamp)
                        .put("userMessage", createHTMLMessage(sender, message, timestamp))
                        .put("userlist", usernameList)
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Creates a message in HTMl Format with given parameters. E.g. name
     * should be bold etc.
     * <p>
     *
     * @param sender    sender's username
     * @param message   text message
     * @param timestamp timestamp when the message where send
     * @return a {@link String} with the HTML script for the web client.
     */
    private static String createHTMLMessage(String sender, String message, String timestamp) {
        return article()
                .with(
                        b(sender),
                        p(message),
                        small(timestamp).withClass("text-muted"))
                .render();
    }
}
