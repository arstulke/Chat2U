package de.chat2u;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;

/**
 * Created Chat in PACKAGE_NAME
 * by ARSTULKE on 16.11.2016.
 */
public class Chat {
    static Map<Session, String> users = new ConcurrentHashMap<>();

    public static void broadcastMessage(String sender, String message) {
        users.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", createHTMLMessage(sender, message))
                        .put("userlist", users.values())
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static String createHTMLMessage(String sender, String message) {
        return article()
                .with(
                        b(sender),
                        p(message),
                        small(new SimpleDateFormat("HH:mm:ss").format(new Date())).withClass("text-muted"))
                .render();
    }
}
