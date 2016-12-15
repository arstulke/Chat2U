package de.chat2u.network;

import de.chat2u.ChatServer;
import de.chat2u.model.users.OnlineUser;
import de.chat2u.model.users.User;
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

import static de.chat2u.ChatServer.chats;

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
            handleCommandFromClient(webSocketSession, new JSONObject(message));
        } catch (JSONException e) {
            if(!message.equals("."))
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
     * @param messageObject    ist die Nachricht des Clients im JSOn Format
     * @throws JSONException wenn die Nachricht kein JSON ist oder ein falsches Format hat
     * @throws IOException   wenn keine Nachricht zurück an den Client gesenet werden kann.
     */
    private void handleCommandFromClient(Session webSocketSession, JSONObject messageObject) throws IOException, JSONException {
        JSONObject params = (JSONObject) messageObject.get("params");
        OnlineUser user = ChatServer.getUserBySession(webSocketSession);

        String cmd = (String) messageObject.get("cmd");
        switch (cmd) {
            case "register": {
                JSONObject msg = ChatServer.register((String) params.get("username"), (String) params.get("passwort"));
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
                if (chats.getChatByID(params.getString("chatID")).getUsers().contains(user)) {
                    String sender = user.getUsername();
                    String message = params.getString("message");
                    if (message.trim().length() > 0) {
                        ChatServer.sendTextMessageToChat(sender, message, params.getString("chatID"));
                    }
                }
                break;
            case "channelOp":
                switch (params.getString("op")) {
                    case "join":
                        ChatServer.joinChannel(params.getString("chatID"), user);
                        break;

                    case "left":
                        ChatServer.leftChannel(params.getString("chatID"), user);
                        break;
                }
                break;
            case "openChat":
                Collection<User> users = new ArrayList<>();
                JSONArray userList = (JSONArray) params.get("users");
                for (int i = 0; i < userList.length(); i++) {
                    String username = userList.getJSONObject(i).getString("name");
                    if (ChatServer.getOnlineUsers().getUsernameList().contains(username))
                        users.add(ChatServer.getOnlineUsers().getByUsername(username));
                }

                if (users.contains(user)) {
                    synchronized (this) {
                        String chatID = ChatServer.createGroup((String) params.get("chatName"), users);
                        if (chatID != null) ChatServer.inviteUserToChat(chatID);
                    }
                } else {
                    JSONObject msg = MessageBuilder.buildMessage("other", "blocked", "Du bist nicht in dem Chat enthalten.");
                    ChatServer.sendMessageToSession(msg, webSocketSession);
                }
                break;
        }
    }
}
