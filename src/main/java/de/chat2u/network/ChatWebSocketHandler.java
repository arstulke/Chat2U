package de.chat2u.network;

import de.chat2u.ChatServer;
import de.chat2u.model.User;
import de.chat2u.utils.MessageBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static de.chat2u.ChatServer.chats;

/**
 * Created ChatWebsocketHandler in de.chat2u
 * by ARSTULKE on 18.11.2016.
 */
@SuppressWarnings("unused") //werden von Spark aufgerufen
@WebSocket
public class ChatWebSocketHandler {

    private final static Logger LOGGER = LogManager.getLogger(ChatWebSocketHandler.class);

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
    public void onDisconnect(Session webSocketSession, int statusCode, String reason) throws JSONException {
        String username = ChatServer.getUsernameBySession(webSocketSession);
        ChatServer.logout(username);
    }

    /**
     * Verwaltet eingehende Nachrichten eines Clients
     */
    @OnWebSocketMessage
    public void onMessage(Session webSocketSession, String message) {
        try {
            if (message.startsWith("[")) {
                JSONArray arr = new JSONArray(message);
                for (int i = 0; i < arr.length(); i++) {
                    handleCommandFromClient(webSocketSession, arr.getJSONObject(i));
                }
            } else {
                handleCommandFromClient(webSocketSession, new JSONObject(message));
            }
        } catch (JSONException e) {
            if (!message.equals("."))
                LOGGER.debug(String.format("Fehler beim Verarbeiten der Nachricht(%s): %s", message.replaceAll("\n", ""), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e.getClass().getName() + " - " + e.getMessage());

            String stackTrace = Arrays.toString(e.getStackTrace()).replace("), ", ")\n\t[" + Thread.currentThread().getName() + "] ");
            stackTrace = stackTrace.substring(1, stackTrace.length() - 1);
            LOGGER.error(message + ": \n\t[" + Thread.currentThread().getName() + "] " + stackTrace);
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
     * @param session ist die Session des Clients
     * @param messageObject    ist die Nachricht des Clients im JSOn Format
     * @throws JSONException wenn die Nachricht kein JSON ist oder ein falsches Format hat
     * @throws IOException   wenn keine Nachricht zurück an den Client gesenet werden kann.
     */
    private void handleCommandFromClient(Session session, JSONObject messageObject) throws IOException, JSONException {
        JSONObject params = (JSONObject) messageObject.get("params");
        String cmd = (String) messageObject.get("cmd");

        User user = null;
        if (!cmd.equals("register") && !cmd.equals("login")) {
            String username = ChatServer.getUsernameBySession(session);
            user = ChatServer.userDataBase.getByUsername(username);
        }

        switch (cmd) {
            case "register": {
                JSONObject msg = ChatServer.register((String) params.get("username"), (String) params.get("passwort"));
                ChatServer.sendMessageToSession(msg, session);
                break;
            }
            case "login": {
                ChatServer.login((String) params.get("username"), (String) params.get("passwort"), session);
                break;
            }
            case "logout":
                ChatServer.logout(user.getUsername());
                session.getRemote().sendString("Goodbye " + user.getUsername());
                break;
            case "sendMessage":
                if (chats.getChatByID(params.getString("chatID")).contains(user.getUsername())) {
                    String sender = user.getUsername();
                    String message = params.getString("message");
                    if (message.trim().length() > 0) {
                        LOGGER.info(user.getUsername() + " send the text message:(" + message + ")");
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
                Set<User> users = new HashSet<>();
                JSONArray userList = (JSONArray) params.get("users");
                for (int i = 0; i < userList.length(); i++) {
                    String username = userList.getJSONObject(i).getString("name");
                    if (ChatServer.getOnlineUsers().contains(username))
                        users.add(ChatServer.userDataBase.getByUsername(username));
                }

                if (users.contains(user)) {
                    String chatID = ChatServer.createGroup((String) params.get("chatName"), users);
                    if (chatID != null) ChatServer.inviteUserToChat(chatID);
                } else {
                    JSONObject msg = MessageBuilder.buildMessage("other", "blocked", "Du bist nicht in dem Chat enthalten.");
                    ChatServer.sendMessageToSession(msg, session);
                }
                break;
        }
    }
}
