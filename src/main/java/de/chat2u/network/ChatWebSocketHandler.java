package de.chat2u.network;

import de.chat2u.ChatServer;
import de.chat2u.model.User;
import de.chat2u.utils.MessageBuilder;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created ChatWebsocketHandler in de.chat2u
 * by ARSTULKE on 18.11.2016.
 */
@SuppressWarnings("unused") //werden von Spark aufgerufen
@WebSocket
public class ChatWebSocketHandler {

    private final static Logger LOGGER = Logger.getLogger(ChatWebSocketHandler.class);

    //////////////////////////////////////////////////////////////////////////
    // ERGEBNISSE MIT CARSTEN 21.11.16                                      //
    //////////////////////////////////////////////////////////////////////////
    // Getrenntes Exceptionhandling;                                        //
    // Sprak testen-> Junit; Mock ->> Einzelne Fehlerbehandlung testen      //
    //////////////////////////////////////////////////////////////////////////

    /**
     * Wenn der Client die Verbindung schließt wird dieser {@link ChatServer#logout(String) ausgeloggt}
     *
     * @param reason ist ein Grund
     */
    @OnWebSocketClose
    public void onDisconnect(Session webSocketSession, int statusCode, String reason) {
        String username = ChatServer.getUserBySession(webSocketSession).getUsername();
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
            LOGGER.debug(String.format("Fehler beim Verarbeiten der Nachricht(%s): %s", message.replaceAll("\n", ""), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e);

            String stackTrace = Arrays.toString(e.getStackTrace()).replace("), ", ")\n\t[" + Thread.currentThread().getName() + "] ");
            stackTrace = stackTrace.substring(1, stackTrace.length() - 1);
            LOGGER.debug(message + ": \n\t[" + Thread.currentThread().getName() + "] " + stackTrace);
        }
    }

    /**
     * Liest die Nachricht des Clients aus und handelt nach dieser.
     * z.B.:
     * <p> - Login
     * <p> - Logout
     * <p> - Register
     * <p> - Chat öffnen
     * <p>- Nachrichten senden
     * <p>
     *
     * @param webSocketSession ist die Session des Clients
     * @param message          ist die Nachricht des Clients
     * @throws JSONException wenn die Nachricht kein JSON ist oder ein falsches Format hat
     * @throws IOException   wenn keine Nachricht zurück an den Client gesenet werden kann.
     */
    private void handleCommandFromClient(Session webSocketSession, String message) throws IOException, JSONException {
        JSONObject object = new JSONObject(message);
        JSONObject params = (JSONObject) object.get("params");


        String cmd = (String) object.get("cmd");
        switch (cmd) {
            case "register": {
                String msg = ChatServer.register((String) params.get("username"), (String) params.get("passwort"));
                ChatServer.sendMessageToSession(msg, webSocketSession);
                break;
            }
            case "login": {
                ChatServer.login((String) params.get("username"), (String) params.get("passwort"), webSocketSession);
                break;
            }
            case "logout":
                ChatServer.logout((String) params.get("username"));
                break;
            case "sendMessage":
                String sender = ChatServer.getUserBySession(webSocketSession).getUsername();
                ChatServer.sendTextMessageToChat(sender, (String) params.get("message"), (String) params.get("chatID"));
                break;
            case "openChat":
                Collection<User> users = new ArrayList<>();
                JSONArray userList = (JSONArray) params.get("users");
                for (int i = 0; i < userList.length(); i++) {
                    String username = (String) ((JSONObject) userList.get(i)).get("name");
                    if (ChatServer.getOnlineUsers().getUsernameList().contains(username))
                        users.add(ChatServer.getOnlineUsers().getByUsername(username));
                }

                if (users.contains(ChatServer.getUserBySession(webSocketSession))) {
                    synchronized (this) {
                        String chatID = ChatServer.createChat(users, (String) params.get("chatName"));
                        if (chatID != null) ChatServer.inviteUser(chatID);
                    }
                } else {
                    JSONObject msg = MessageBuilder.buildMessage("other", "blocked", "Du bist nicht in dem Chat enthalten.");
                    ChatServer.sendMessageToSession(msg.toString(), webSocketSession);
                }
                break;
        }
    }
}
