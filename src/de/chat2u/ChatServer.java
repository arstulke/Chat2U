package de.chat2u;

import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.Permissions;
import de.chat2u.authentication.UserRepository;
import de.chat2u.exceptions.AccessDeniedException;
import de.chat2u.exceptions.UsernameExistException;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.model.User;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

/**
 * Bevor der {@link ChatServer} benutzt werden kann, muss die {@link ChatServer#initialize(AuthenticationService)}
 * aufgerufen werden. Sonst wird eine {@link IllegalStateException} geschmissen. Als Beispielcode:
 * <p>{@code ChatServer.initialize(new AuthenticationService(new UserRepository<>()));}
 * <p>{@code ChatServer.initialize(new AuthenticationService(#YourFooRepository));}
 */
public class ChatServer {

    private final static UserRepository<User> onlineUsers = new UserRepository<>();
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
        return "Registrierung erfolgreich; '" + authUser.getUsername() + "' ist " + authUser.getPermissions();
    }

    /**
     * Loggt einen registrierten AuthenticationUser ein, mit den gegebenen Parametern
     * AuthenticationUser und Password.
     * <p>
     *
     * @param username ist der eindeutige Username des Benutzers
     * @param password ist das Passwort zu dem Username
     * @return die Antwort der Datenbank über erfolg ("Gültige Zugangsdaten")
     * @throws AccessDeniedException wenn der AuthenticationUser nicht mit den gegebenen
     *                               Parametern eingetragen ist. Die Message ist ("Ungültige Zugangsdaten")
     */
    public static String login(String username, String password, Session userSession) throws AccessDeniedException {
        checksIllegalState();

        AuthenticationUser user = authenticationService.authenticate(username, password);
        if (user != null) {
            user.setSession(userSession);
            onlineUsers.addUser(user.getSimpleUser());
            broadcastTextMessage("Server", user.getUsername() + " joined the Server");
            return "Gültige Zugangsdaten";
        }
        throw new AccessDeniedException("Ungültige Zugangsdaten");
    }

    /**
     * Entfernt den User aus der Liste der Benutzer, welche online sind.
     * <p>
     *
     * @param username ist der eindeutige username des Benutzers.
     */
    public static void logout(String username) {
        onlineUsers.removeUser(onlineUsers.getByUsername(username));
        broadcastTextMessage("Server", username + " left the Server");
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
    public static void broadcastTextMessage(String sender, String message) {
        String msg = Utils.buildMessage(sender, message);
        onlineUsers.getUsernameList().forEach(username -> {
            try {
                User user = onlineUsers.getByUsername(username);

                user.addMessageToHistory(msg);
                sendMessageToSession(msg, user.getSession());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    private static void checksIllegalState() {
        if (authenticationService == null)
            throw new IllegalStateException("You have to use Chatserver.initialize() method first.");
    }

    public static User getRegisteredUserByName(String username) {
        return authenticationService.getUserByName(username);
    }

    public static User getUsernameBySession(Session webSocketSession) {
        return onlineUsers.getBySession(webSocketSession);
    }
}
