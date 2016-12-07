package de.chat2u;

import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.*;
import de.chat2u.utils.MessageBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static de.chat2u.utils.MessageBuilder.buildMessage;

/**
 * Bevor der {@link ChatServer} benutzt werden kann, muss die {@link ChatServer#initialize(AuthenticationService)}
 * aufgerufen werden. Sonst wird eine {@link IllegalStateException} geschmissen. Als Beispielcode:
 * <p>{@code ChatServer.initialize(new AuthenticationService(new UserRepository<>()));}
 * <p>{@code ChatServer.initialize(new AuthenticationService(#YourFooRepository));}
 */
public class ChatServer {

    private final static UserRepository<User> onlineUsers = new UserRepository<>();
    private final static ChatContainer chats = new ChatContainer();
    public static final String GLOBAL = "global";
    private static AuthenticationService authenticationService;

    /**
     * Initialiesiert die Inneren statischen Objekte
     * <p>
     *
     * @param authenticationService ist der AuthentifizierungsService, welcher alle
     *                              registrierten User und Berechtigungsschlüssel enthält
     */
    public static void initialize(AuthenticationService authenticationService) {
        ChatServer.authenticationService = authenticationService;
    }

    private static void checksIllegalState() {
        if (authenticationService == null)
            throw new IllegalStateException("You have to use ChatServer.initialize() method first.");
    }

    //region  Register, Login, Logout

    /**
     * Registriert einen Benutzer anhand des Passworts und des Benuzternamens mit Berechtigungen.
     * Wenn dies fehlschlägt, aufgrund von vergebenen Benutzernamens, dann wird
     * eine Exception geschmissen.
     * <p>
     *
     * @param username ist der username für den neuen Benutzer
     * @param password ist das passwort für den neuen Benutzer
     * @return eine Nachricht über erfolg, die angezeigt werden kann
     * @see AuthenticationService#addUser(AuthenticationUser) AuthenticationService für weitere Infos.
     */
    public static String register(String username, String password) {
        checksIllegalState();

        if (username == null || username.length() <= 3) {
            return buildMessage("statusRegister", false, "Benutzername ist zu kurz.").toString();
        }

        if (authenticationService.userExist(username))
            return buildMessage("statusRegister", false, "Benutzername bereits vergeben.").toString();
        AuthenticationUser authUser = new AuthenticationUser(username, password);
        authenticationService.addUser(authUser);
        return buildMessage("statusRegister", true, null).toString();
    }

    /**
     * Loggt einen registrierten AuthenticationUser ein, mit den gegebenen Parametern
     * AuthenticationUser und Password.
     * <p>
     *
     * @param username    ist der eindeutige Username des Benutzers
     * @param password    ist das Passwort zu dem Username
     * @param userSession ist die Session des Users zum abspeichern
     * @return die Antwort der Datenbank über erfolg ("Gültige Zugangsdaten")
     */
    public static String login(String username, String password, Session userSession) throws IOException {
        checksIllegalState();

        String msg;
        if (!onlineUsers.containsUsername(username)) {
            AuthenticationUser user = authenticationService.authenticate(username, password);
            if (user != null) {
                user.setSession(userSession);
                User simpleUser = user.getSimpleUser();
                onlineUsers.addUser(simpleUser);
                chats.overwrite(GLOBAL, onlineUsers, true);
                msg = buildMessage("statusLogin", true, null).toString();
                sendMessageToSession(msg, userSession);
                sendTextMessageToChat("Server:", user.getUsername() + " joined the Server", GLOBAL);
                return msg;
            } else {
                msg = MessageBuilder.buildMessage("statusLogin", false, "Ungültige Zugangsdaten.").toString();
            }
        } else {
            msg = MessageBuilder.buildMessage("statusLogin", "occupied", "Benutzer bereits angemeldet.").toString();
        }

        sendMessageToSession(msg, userSession);
        return msg;
    }

    /**
     * Entfernt den User aus der Liste der Benutzer, welche online sind.
     * <p>
     *
     * @param username ist der eindeutige username des Benutzers.
     */
    public static void logout(String username) {
        User user = onlineUsers.getByUsername(username);
        onlineUsers.removeUser(user);

        Map<String, Chat> containedChats = new HashMap<>();
        chats.forEach(chat -> {
            if (chat.contains(user)) {
                String chatID = chat.getID();
                chat.removeUser(user);
                containedChats.put(chatID, chat);
            }
        });

        containedChats.entrySet().forEach(chat -> {
            String chatID = chat.getKey();
            if (!chatID.equals(GLOBAL) && chat.getValue().size() == 1) {
                sendTextMessageToChat("Server", username, chatID);
                JSONObject json = buildMessage("tabControl", chatID, "close");
                sendMessageToChat(json, chatID);
                chats.removeChat(chatID);
            }
        });

        sendTextMessageToChat("Server:", username + " left the Server", GLOBAL);
    }

    private static void sendMessageToChat(JSONObject json, String chatID) {
        chats.getChat(chatID).forEach(user -> {
            try {
                sendMessageToSession(json.toString(), user.getSession());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //endregion

    //region send Messages

    /**
     * Sendet eine Nachricht an alle Benutzer in einem Chat
     * <p>
     *
     * @param senderName ist der Benutzername eines Benutzers
     * @param message    ist die Textnachricht die versendet werden soll
     * @param chatID     ist die ID des Chats, in welchem die Nachricht versandt werden soll
     */
    public static void sendTextMessageToChat(String senderName, String message, String chatID) {
        Message msg = new Message(senderName, message, chatID);
        JSONObject json = MessageBuilder.buildTextMessage(msg);
        chats.getChat(chatID).forEach(user -> user.addMessageToHistory(msg));
        sendMessageToChat(json, chatID);
    }

    /**
     * Sendet die Nachricht an eine bestimmte Session, also einem Benutzer.
     * <p>
     *
     * @param session ist die WebSocketSession an welche das gesendet wird.
     * @param msg     ist die zu sendene Nachricht.
     * @throws IOException wenn das senden fehlschlägt.
     */
    public static void sendMessageToSession(String msg, Session session) throws IOException {
        if (session != null)
            session.getRemote().sendString(msg);
    }

    //endregion

    /**
     * Gibt die Liste der User, welche online sind zurück.
     * <p>
     *
     * @return die {@link User} in einem {@link UserRepository}
     */
    public static UserRepository<User> getOnlineUsers() {
        return onlineUsers;
    }

    /**
     * @param webSocketSession ist die Jetty Session des Benutzers
     *                         <p>
     * @return einen {@link User Benutzer}
     */
    public static User getUserBySession(Session webSocketSession) {
        checksIllegalState();
        return onlineUsers.getBySession(webSocketSession);
    }

    /**
     * Erstellt einen neuen Chat
     * <p>
     *
     * @param users sind die User, die zu dem neuen Chat hinzugefügt werden sollen
     *              <p>
     * @return die ChatID
     */
    public static String createChat(UserRepository<User> users) {
        return chats.createNewChat(users);
    }

    public static void inviteUser(UserRepository<User> users, String chatID) throws JSONException {
        String name = users.getUsernameList().toString().replace("[", "").replace("]", "");
        JSONObject json = buildMessage("tabControl", new JSONObject().put("chatID", chatID).put("name", name), "open");
        String message = json.toString();
        users.forEach(user -> {
            try {
                sendMessageToSession(message, user.getSession());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
