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
 * SCHNITTSTELLE ZUM SERVER
 * <p>
 * Created ChatServer in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class ChatServer {

    private final static UserRepository<User> onlineUsers = new UserRepository<>();
    private static AuthenticationService authenticationService;

    public ChatServer(AuthenticationService authenticationService) {
        ChatServer.authenticationService = authenticationService;
    }

    //region  Register, Login, Logout
    /**
     * Registriert einen Benutzer anhand des Passworts und des Benuzternamens.
     * Wenn dies fehlschlägt, aufgrund von vergebenen Benutzernamens, dann wird
     * eine Exception geschmissen.
     * <p>
     *
     * @param nickname ist der username für den neuen Benutzer
     * @param password ist das passwort für den neuen Benutzer
     * @return eine Nachricht über erfolg, die angezeigt werden kann
     * @throws UsernameExistException wenn der Benutzer bereits existiert
     * @see AuthenticationService#addUser(AuthenticationUser) AuthenticationService für weitere Infos.
     */
    public static String register(String nickname, String password) {
        Permissions permissions = Permissions.USER;

        AuthenticationUser authUser = new AuthenticationUser(nickname, password, permissions);
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
        AuthenticationUser authenticationUser = authenticationService.authenticate(username, password);
        if (authenticationUser != null) {
            authenticationUser.setSession(userSession);
            onlineUsers.addUser(authenticationUser.getSimpleUser());
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
    public static void broadcastMessage(String sender, String message) {
        String msg = buildMessage(sender, message);
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
     * Generieren der zu sendenen Nachricht. Die Nachricht wird zusammengesetzt aus
     * <p>
     * - Absender
     * <p>
     * - Nachricht
     * <p>
     * <p>
     *
     * @param message ist die zu sendene Nachricht
     * @param sender  ist der Absender der Nachricht
     * @return die fertig generierte Nachricht.
     */
    private static String buildMessage(String sender, String message) {
        return sender + ": " + message;
    }

    /**
     * Sendet die Nachricht an eine bestimmte Session, also einem Benutzer.
     * <p>
     *
     * @param session ist die WebSocketSession an welche das gesendet wird.
     * @param msg     ist die zu sendene Nachricht.
     * @throws IOException wenn das senden fehlschlägt.
     */
    private static void sendMessageToSession(String msg, Session session) throws IOException {
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
}
