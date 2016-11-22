package de.chat2u.network;

import de.chat2u.ChatServer;
import de.chat2u.utils.MessageBuilder;
import de.chat2u.utils.NetworkUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created ChatWebsocketHandler in de.chat2u
 * by ARSTULKE on 18.11.2016.
 */
@SuppressWarnings("unused") //werden von Spark aufgerufen
@WebSocket
public class ChatWebSocketHandler {

    /**
     * Wenn ein Client eine Verbindung aufbaut wird versucht diesen einzuloggen,
     * wenn dies nicht gelingt passiert nichts.
     */
    @OnWebSocketConnect
    public void onConnect(Session webSocketSession) {
        if (((WebSocketSession) webSocketSession).getRequestURI().getQuery() != null) {
            HashMap<String, String> params = NetworkUtils.getParams(((WebSocketSession) webSocketSession).getRequestURI().getQuery());
            try {
                if (params.containsKey("username") && params.containsKey("password")) {
                    String msg = ChatServer.login(params.get("username"), params.get("password"), webSocketSession);
                    ChatServer.sendMessageToSession(msg, webSocketSession);
                }
            } catch (Exception exception) {
                String msg = MessageBuilder.buildExceptionMessage(exception);
                System.err.println(exception.getMessage());

                //////////////////////////////////////////////////////////////////////////
                // ERGEBNISSE MIT CARSTEN 21.11.16                                      //
                //////////////////////////////////////////////////////////////////////////
                // Loggen statt System.(out/err)                                        //
                // Getrenntes Exceptionhandling;                                        //
                // Sprak testen-> Junit; Mock ->> Einzelne Fehlerbehandlung testen      //
                //////////////////////////////////////////////////////////////////////////

                try {
                    ChatServer.sendMessageToSession(msg, webSocketSession);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Wenn der Client die Verbindung schließt wird dieser {@link ChatServer#logout(String) ausgeloggt}
     */
    @OnWebSocketClose
    public void onDisconnect(Session webSocketSession, int statusCode, String reason) {
        String username = ChatServer.getUsernameBySession(webSocketSession).getUsername();
        ChatServer.logout(username);
    }

    /**
     * Verwaltet eingehende Nachrichten eines Clients
     */
    @OnWebSocketMessage
    public void onMessage(Session webSocketSession, String message) {
        try {
            handleCommandFromClient(webSocketSession, message);
        } catch (JSONException e) {
            String sender = ChatServer.getUsernameBySession(webSocketSession).getUsername();
            ChatServer.broadcastTextMessage(sender + ":", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * List die nachricht des Clients aus und handelt nach dieser.
     * z.B.:
     * <p>   Login
     * <p>   Logout
     * Register
     * <p>
     *
     * @param webSocketSession ist die Session des Clients
     * @param message          ist die Nachricht des Clients
     * @throws JSONException wenn die Nachricht kein JSON ist oder ein falsches Format hat
     * @throws IOException   wenn keine Nachricht zurück an den Client gesenet werden kann.
     */
    private void handleCommandFromClient(Session webSocketSession, String message) throws JSONException, IOException {
        JSONObject object = new JSONObject(message);
        JSONObject params = (JSONObject) object.get("params");
        if (object.get("cmd").equals("login")) {
            String msg = ChatServer.login((String) params.get("username"), (String) params.get("passwort"), webSocketSession);
            ChatServer.sendMessageToSession(msg, webSocketSession);
        } else if (object.get("cmd").equals("logout")) {
            ChatServer.logout((String) params.get("username"));
        } else if (object.get("cmd").equals("register")) {
            String msg = ChatServer.register((String) params.get("username"), (String) params.get("passwort"));
            ChatServer.sendMessageToSession(msg, webSocketSession);
        }
    }
}
