package de.chat2u;

import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

/**
 * Created Chat in PACKAGE_NAME
 * by ARSTULKE on 16.11.2016.
 */
public class Chat {
    static Map<Session, User> users = new ConcurrentHashMap<>();

    public static void broadcastMessage(String sender, String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());

        List<String> usernameList = getUsernameList();
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

    private static String createHTMLMessage(String sender, String message, String timestamp) {
        return article()
                .with(
                        b(sender),
                        p(message),
                        small(timestamp).withClass("text-muted"))
                .render();
    }

    public static List<String> getUsernameList() {
        return users.values().stream().map(User::getUsername).collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<String> getUserTokens() {
        return users.values().stream().map(User::getToken).collect(Collectors.toCollection(ArrayList::new));
    }

    public static String generateUniqueToken() {
        List<String> tokens = getUserTokens();
        String token = generateRandomString();
        while (tokens.contains(token))
            token = generateRandomString();
        return token;
    }

    private static String generateRandomString() {
        return RandomStringUtils.random(32, true, true);
    }
}
