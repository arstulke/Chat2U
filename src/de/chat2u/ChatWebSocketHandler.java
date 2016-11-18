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

    /**
     * Is called when a Client connects to Server. It checks
     * if the given Username is already taken or if it is too
     * short. Else it broadcastg to everyone, that a user joins.
     * <p>
     *
     * @param user is the query from the URL with the given keys and
     *             values
     * */
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        HashMap<String, String> params = Utils.getParams(((WebSocketSession) user).getRequestURI().getQuery());
        String username = params.get("username");

        if (username == null || username.length() < 1) {
            user.getRemote().sendString("/msg=Username to short.");
        }

        if (Chat.users.size() > 0 && Utils.getUsernameList().contains(username)) {
            user.getRemote().sendString("/msg=Username is already used.");
        } else if (username != null && username.length() >= 1) {
            Chat.users.put(user, new User(username, Utils.generateUniqueToken()));
            Chat.broadcastMessage("Server", username + " joined the chat");
        }
    }


    /**
     * This Method is called when a User disconnects. The given parameters
     * aren't necessary.
     * <p>
     *
     * @param user is the user, who disconnects
     * @param reason is the reason, hwy he disconnects
     * @param statusCode is the StatusCode of thier disconect
     * */
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = Chat.users.get(user).getUsername();
        if (username != null && username.length() >= 1) {
            Chat.users.remove(user);
            Chat.broadcastMessage("Server", username + " left the chat");
        }
    }

    /**
     * Is called when the Server receives a message from a client. The method
     * decides whether it is a serverCommand or a textMessage.
     * <p>
     *
     * @param user is the user, who sends a message
     * @param message is the message from the user
     * */
    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        if (message.startsWith("/")) {
            executeCommand(message);
        } else {
            Chat.broadcastMessage(Chat.users.get(user).getUsername(), message);
        }
    }

    /**
     * Executes a command which where called  by the Client.
     *
     * @param command To executed Command.
     * */
    private void executeCommand(String command) {

    }
}
