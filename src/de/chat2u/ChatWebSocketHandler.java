package de.chat2u;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.common.WebSocketSession;

import java.util.HashMap;

/**
 * Created ChatWebSocketHandler in PACKAGE_NAME
 * by ARSTULKE on 16.11.2016.
 */
@SuppressWarnings("unused")
@WebSocket
public class ChatWebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        HashMap<String, String> params = Utils.getParams(((WebSocketSession) user).getRequestURI().getQuery());
        String username = params.get("username");

        if (username == null || username.length() < 1) {
            user.getRemote().sendString("/msg=Username to short.");
        }

        if (Chat.users.size() > 0 && Chat.getUsernameList().contains(username)) {
            user.getRemote().sendString("/msg=Username is already used.");
        } else if (username != null && username.length() >= 1) {
            Chat.users.put(user, new User(username, Chat.generateUniqueToken()));
            Chat.broadcastMessage("Server", username + " joined the chat");
        }
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = Chat.users.get(user).getUsername();
        if (username != null && username.length() >= 1) {
            Chat.users.remove(user);
            Chat.broadcastMessage("Server", username + " left the chat");
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        Chat.broadcastMessage(Chat.users.get(user).getUsername(), message);
    }
}
