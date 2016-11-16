package de.chat2u;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

/**
 * Created ChatWebSocketHandler in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class ChatWebSocketHandler {

    private static String message;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {

    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
     ChatWebSocketHandler.message = message;
    }

    public static String getMessage(){
        return message;
    }
}
