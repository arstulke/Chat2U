package de.chat2u;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.common.WebSocketSession;

import java.util.HashMap;

/**
 * Created ChatWebSocketHandler in PACKAGE_NAME
 * by ARSTULKE on 16.11.2016.
 */
@WebSocket
public class ChatWebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        HashMap<String, String> params = getParams(((WebSocketSession) user).getRequestURI().getQuery());
        String username = params.get("username");

        if (Chat.users.containsValue(username)) {
            user.getRemote().sendString("/msg=Username already used.");
        } else if (username != null && username.length() >= 1) {
            Chat.users.put(user, username);
            Chat.broadcastMessage("Server", username + " joined the chat");
        }
    }

    private HashMap<String, String> getParams(String query) {
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

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = Chat.users.get(user);
        Chat.users.remove(user);
        Chat.broadcastMessage("Server", username + " left the chat");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        Chat.broadcastMessage(Chat.users.get(user), message);
    }
}
