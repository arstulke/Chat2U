package de.chat2u.network;

import de.chat2u.ChatServer;
import de.chat2u.Utils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.common.WebSocketSession;

import java.util.HashMap;

/**
 * Created ChatWebsocketHandler in de.chat2u
 * by ARSTULKE on 18.11.2016.
 */
@WebSocket
public class ChatWebsocketHandler {
    @OnWebSocketConnect
    public void onConnect(Session webSocketSession) {
        HashMap<String, String> params = Utils.getParams(((WebSocketSession) webSocketSession).getRequestURI().getQuery());
        ChatServer.login(params.get("username"), params.get("password"), webSocketSession);
    }

    @OnWebSocketClose
    public void onDisconnect(Session webSocketSession, int statusCode, String reason){
        String username = ChatServer.getUsernameBySession(webSocketSession).getUsername();
        ChatServer.logout(username);
    }

    @OnWebSocketMessage
    public void onMessage(Session webSocketSession, String message){
        String sender = ChatServer.getUsernameBySession(webSocketSession).getUsername();
        ChatServer.broadcastTextMessage(sender, message);
    }
}
