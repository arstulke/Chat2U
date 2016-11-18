package de.chat2u;

import j2html.TagCreator;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static j2html.TagCreator.*;

public class Utils {
    /**
     * Get Query params from Request/Query String
     *
     * @param query Querystring from Request
     * @return a Hashmap with the querxKey as Key and the value as value
     */
    public static HashMap<String, String> getParams(String query) {
        HashMap<String, String> parameters = new HashMap<>();
        String[] params = query.split("&");

        for (String param : params) {
            if (param.contains("=")) {
                if (param.split("=").length > 1) {
                    parameters.put(param.split("=")[0], param.split("=")[1]);
                } else {
                    parameters.put(param, "");
                }
            } else {
                parameters.put(param, "");
            }
        }
        return parameters;
    }

    /**
     * Generieren der zu sendenen Nachricht. Die Nachricht wird zusammengesetzt aus
     * <p>- Absender
     * <p>- Nachricht
     * <p>
     *
     * @param message ist die zu sendene Nachricht
     * @param sender  ist der Absender der Nachricht
     * @return die fertig generierte Nachricht.
     */
    public static String buildMessage(String sender, String message) {
        try {
            String timestamp = new SimpleDateFormat("hh:mm dd.MM.yyyy").format(new Date());
            return String.valueOf(new JSONObject()
                    .put("type", "msg")
                    .put("sender", sender)
                    .put("timestamp", timestamp)
                    .put("userMessage", createHTMLMessage(sender, message, timestamp))
                    .put("userlist", ChatServer.getOnlineUsers().getUsernameList())
            );
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
    private static String createHTMLMessage(String sender, String message, String timestamp) {
        return TagCreator.article()
                .with(
                        b(sender),
                        p(message),
                        small(timestamp).withClass("text-muted"))
                .render();
    }

    public static String buildExceptionMessage(Exception exception) {
        try {
            return String.valueOf(new JSONObject()
                    .put("type", "error")
                    .put("exceptionType", exception.getClass().getSimpleName())
                    .put("exceptionMessage", exception.getMessage())
                    .put("msg", "<p style=\"color:#F70505\">" + exception.getMessage() + "</p>"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}