package de.chat2u;

import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.Permissions;
import de.chat2u.authentication.UserRepository;
import de.chat2u.exceptions.AccessDeniedException;
import de.chat2u.exceptions.UsernameExistException;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.model.ChatContainer;
import de.chat2u.model.Message;
import de.chat2u.model.User;
import de.chat2u.utils.MessageBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

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
     * Registriert einen Benutzer anhand des Passworts und des Benuzternamens.
     * Wenn dies fehlschlägt, aufgrund von vergebenen Benutzernamens, dann wird
     * eine Exception geschmissen.
     * <p>
     *
     * @param username ist der username für den neuen Benutzer
     * @param password ist das passwort für den neuen Benutzer
     * @return eine Nachricht über erfolg, die angezeigt werden kann
     * @throws UsernameExistException wenn der Benutzer bereits existiert
     * @see AuthenticationService#addUser(AuthenticationUser) AuthenticationService für weitere Infos.
     */
    public static String register(String username, String password) {
        return register(username, password, null);
    }

    /**
     * Registriert einen Benutzer anhand des Passworts und des Benuzternamens mit Berechtigungen.
     * Wenn dies fehlschlägt, aufgrund von vergebenen Benutzernamens, dann wird
     * eine Exception geschmissen.
     * <p>
     *
     * @param username ist der username für den neuen Benutzer
     * @param password ist das passwort für den neuen Benutzer
     * @param token    ist der Berechtigungsschlüssel für ein Berechtigunslevel
     * @return eine Nachricht über erfolg, die angezeigt werden kann
     * @throws UsernameExistException wenn der Benutzer bereits existiert
     * @see AuthenticationService#addUser(AuthenticationUser) AuthenticationService für weitere Infos.
     */
    public static String register(String username, String password, String token) {
        checksIllegalState();

        if (username == null || username.length() < 1) {
            throw new IllegalArgumentException("Username too short.");
        }

        Permissions permissions;
        if (!authenticationService.userExist(username))
            permissions = authenticationService.verifyToken(token);
        else
            throw new UsernameExistException("Benutzername bereits vergeben");
        AuthenticationUser authUser = new AuthenticationUser(username, password, permissions);
        authenticationService.addUser(authUser);
        return "{\"type\":\"server_msg\",\"msg\":\"Registrieren erfolgreich\"}";
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
     * @throws AccessDeniedException wenn der AuthenticationUser nicht mit den gegebenen
     *                               Parametern eingetragen ist. Die Message ist ("Ungültige Zugangsdaten")
     */
    public static String login(String username, String password, Session userSession) throws AccessDeniedException {
        checksIllegalState();

        if (!onlineUsers.containsUsername(username)) {
            AuthenticationUser user = authenticationService.authenticate(username, password);
            if (user != null) {
                user.setSession(userSession);
                User simpleUser = user.getSimpleUser();
                onlineUsers.addUser(simpleUser);
                chats.overwrite(GLOBAL, onlineUsers);
                sendMessageToGlobalChat("Server:", user.getUsername() + " joined the Server");
                return "{\"type\":\"server_msg\",\"msg\":\"Gültige Zugangsdaten\"}";
            }
            throw new AccessDeniedException("Ungültige Zugangsdaten");
        }
        throw new AccessDeniedException("Benutzer bereits angemeldet");
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
        chats.forEach(chat -> {
            if (chat.contains(user)) {
                chat.removeUser(user);
                if (chat.size() == 0) {
                    chats.removeChat(String.valueOf(chat.hashCode()));
                }
            }
        });
        sendMessageToGlobalChat("Server:", username + " left the Server");
    }

    //endregion

    //region send Messages

    /**
     * Sendet eine Nachricht an alle Personen, welche online sind.
     * Bei fehlschlagen wird ein StackTrace geprintet und der Error
     * wird ignoriert.
     * <p>
     *
     * @param sender  ist der Absender der Nachricht
     * @param message ist die zu sendene Nachricht
     */
    public static void sendMessageToGlobalChat(String sender, String message) {
        sendMessageToChat(sender, message, GLOBAL);
    }

    /**
     * Sendet eine Nachricht an alle Benutzer in einem Chat
     * <p>
     *
     * @param senderName ist der Benutzername eines Benutzers
     * @param msg        ist die Textnachricht die versendet werden soll
     * @param chatID     ist die ID des Chats, in welchem die Nachricht versandt werden soll
     */
    public static void sendMessageToChat(String senderName, String msg, String chatID) {
        Message message = new Message(senderName, msg, chatID);
        chats.getChat(chatID).sendMessage(message);
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
     * Generiert einen einzigartigen Token.
     * <p>
     *
     * @param permissions ist das Berechtigungslevel, welches der Nutzer bekommt wenn er den generierten Token verwendet.
     * @return den generierten Token
     */
    public static String generateToken(Permissions permissions) {
        checksIllegalState();
        return authenticationService.generateToken(permissions);
    }

    /**
     * @param username ist der Benutzername des zu suchenden Benutzers
     *                 <p>
     * @return einen Registrierten {@link User Benutzer}
     */
    public static User getRegisteredUserByName(String username) {
        return authenticationService.getUserByName(username);
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
        String userList = users.getUsernameList().toString().replace("[", "{").replace("]", "}");
        String msg = "You were invited to a new Chat.<br>The users are " + userList;
        JSONObject json = MessageBuilder.buildMessage(new Message("Server", msg, chatID), "server_msg");
        json.put("invite", chatID);
        json.remove("chatID");
        json.put("name", userList.replace("{", "").replace("}", ""));
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
