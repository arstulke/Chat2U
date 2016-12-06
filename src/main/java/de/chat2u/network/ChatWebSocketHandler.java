package de.chat2u.network;

import de.chat2u.ChatServer;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.User;
import de.chat2u.utils.MessageBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created ChatWebsocketHandler in de.chat2u
 * by ARSTULKE on 18.11.2016.
 */
@SuppressWarnings("unused") //werden von Spark aufgerufen
@WebSocket
public class ChatWebSocketHandler {

    //////////////////////////////////////////////////////////////////////////
    // ERGEBNISSE MIT CARSTEN 21.11.16                                      //
    //////////////////////////////////////////////////////////////////////////
    // Getrenntes Exceptionhandling;                                        //
    // Sprak testen-> Junit; Mock ->> Einzelne Fehlerbehandlung testen      //
    //////////////////////////////////////////////////////////////////////////

    /**
     * Wenn der Client die Verbindung schließt wird dieser {@link ChatServer#logout(String) ausgeloggt}
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
            String sender = ChatServer.getUserBySession(webSocketSession).getUsername();
            ChatServer.sendMessageToGlobalChat(sender + ":", message, "msg");
        } catch (Exception exception) {
            try {
                ChatServer.sendMessageToSession(MessageBuilder.buildExceptionMessage(exception), webSocketSession);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    private void handleCommandFromClient(Session webSocketSession, String message) throws JSONException, IOException {
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
                ChatServer.sendMessageToChat(sender, (String) params.get("message"), (String) params.get("chatID"), "msg");
                break;
            case "openChat":
                UserRepository<User> users = new UserRepository<>();
                JSONArray userList = (JSONArray) params.get("users");
                for (int i = 0; i < userList.length(); i++) {
                    String username = (String) ((JSONObject) userList.get(i)).get("name");
                    if (ChatServer.getOnlineUsers().getUsernameList().contains(username))
                        users.addUser(ChatServer.getOnlineUsers().getByUsername(username));
                }
                String chatID = ChatServer.createChat(users);
                if (chatID != null) ChatServer.inviteUser(users, chatID);
                break;
        }
    }
}
