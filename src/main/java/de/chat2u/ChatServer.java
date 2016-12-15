package de.chat2u;

import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.ChatContainer;
import de.chat2u.model.Message;
import de.chat2u.model.chats.Channel;
import de.chat2u.model.chats.Chat;
import de.chat2u.model.chats.Group;
import de.chat2u.model.users.AuthenticationUser;
import de.chat2u.model.users.OnlineUser;
import de.chat2u.model.users.User;
import de.chat2u.utils.MessageBuilder;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;

import static de.chat2u.utils.MessageBuilder.buildMessage;
import static de.chat2u.utils.MessageBuilder.buildTextMessage;
import static j2html.TagCreator.*;

/**
 * Bevor der {@link ChatServer} benutzt werden kann, muss die {@link ChatServer#initialize(AuthenticationService)}
 * aufgerufen werden. Sonst wird eine {@link IllegalStateException} geschmissen. Als Beispielcode:
 * <p>{@code ChatServer.initialize(new AuthenticationService(new UserRepository<>()));}
 * <p>{@code ChatServer.initialize(new AuthenticationService(#YourFooRepository));}
 */
public class ChatServer {
    private final static Logger LOGGER = Logger.getLogger(ChatServer.class);

    private static AuthenticationService authenticationService;
    private final static UserRepository<OnlineUser> onlineUsers = new UserRepository<>();
    public final static ChatContainer chats = new ChatContainer();

    private static final String Lobby = "Lobby";
    public static final String LobbyID = String.valueOf(Lobby.hashCode());

    /**
     * Initialiesiert die Inneren statischen Objekte
     * <p>
     *
     * @param authenticationService ist der AuthentifizierungsService, welcher alle
     *                              registrierten User und Berechtigungsschlüssel enthält
     */
    public static void initialize(AuthenticationService authenticationService) {
        ChatServer.authenticationService = authenticationService;
        chats.createNewChannel(Lobby);
        chats.createNewChannel("Informatik");
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
    public static JSONObject register(String username, String password) {
        checksIllegalState();

        if (username == null || username.length() <= 3) {
            return buildMessage("statusRegister", false, "Benutzername ist zu kurz.");
        }

        if (authenticationService.userExist(username))
            return buildMessage("statusRegister", false, "Benutzername bereits vergeben.");
        AuthenticationUser authUser = new AuthenticationUser(username, password);
        authenticationService.addUser(authUser);
        return buildMessage("statusRegister", true, null);
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
    public static String login(String username, String password, Session userSession) {
        checksIllegalState();

        JSONObject msg;
        if (!onlineUsers.containsUsername(username)) {
            User offlineUser = authenticationService.authenticate(username, password);
            if (offlineUser != null) {
                OnlineUser user = offlineUser.toOnlineUser(userSession);
                onlineUsers.addUser(user);
                msg = buildMessage("statusLogin", true, null);
                sendMessageToSession(msg, userSession);

                Channel chat = (Channel) chats.getChatByID(LobbyID);
                try {
                    chat.addUser(user);
                    sendOpenChatCommand(user, chat);


                    String textMessage = article().with(
                            b("Chat2U"),
                            p("Herzlich Willkommen! Vielen Dank, dass du Chat2U benutzt.")
                    ).render() + hr().render();
                    JSONObject primeData = new JSONObject().put("chatID", ChatServer.LobbyID).put("message", textMessage);
                    JSONObject message = buildMessage("textMessage", primeData, MessageBuilder.getUserList());
                    sendMessageToSession(message, userSession);

                    message = MessageBuilder.buildTextMessage(new Message("Server", user.getUsername() + " joined the Server.", LobbyID));
                    sendMessageToChat(message, LobbyID);
                } catch (JSONException e) {
                    LOGGER.debug(e);
                }

                user.getGroups().forEach(chatID -> {
                    Group group = (Group) chats.getChatByID(chatID);

                    //sendOpenChatCommand
                    sendOpenChatCommand(user, group);
                    group.getHistory().forEach(message -> {
                        JSONObject json = MessageBuilder.buildTextMessage(message);
                        sendMessageToSession(json, userSession);
                    });

                    Message messageObject = new Message("Server", user.getUsername() + " goes online.", chatID);
                    JSONObject message = MessageBuilder.buildTextMessage(messageObject);
                    sendMessageToChat(message, chatID);
                    group.getHistory().add(messageObject);
                });

                return msg.toString();
            } else {
                msg = buildMessage("statusLogin", false, "Ungültige Zugangsdaten.");
            }
        } else {
            msg = buildMessage("statusLogin", "occupied", "Benutzer bereits angemeldet.");
        }

        sendMessageToSession(msg, userSession);
        return msg.toString();
    }

    /**
     * Entfernt den User aus der Liste der Benutzer, welche online sind.
     * <p>
     *
     * @param username ist der eindeutige username des Benutzers.
     */
    public static void logout(String username) {
        OnlineUser user = onlineUsers.getByUsername(username);
        onlineUsers.removeUser(user);

        user.getGroups().forEach(chatID -> {
            Chat chat = chats.getChatByID(chatID);
            if (chat instanceof Group)
                sendTextMessageToChat("Server", username + " goes offline.", chatID);
        });

        chats.getChannels().forEach(channel -> ((Channel) channel).removeUser(user));

        authenticationService.logout(user);

        sendTextMessageToChat("Server:", username + " left the Server", LobbyID);
    }

    private static void sendMessageToChat(JSONObject json, String chatID) {
        Chat chat = chats.getChatByID(chatID);
        chat.forEach(user -> sendMessageToUser(json, user));
    }

    //endregion

    //region SendMessageTo
    private static void sendMessageToUser(JSONObject msg, User user) {
        OnlineUser onlineUser = onlineUsers.getByUsername(user.getUsername());
        if (onlineUser != null)
            sendMessageToSession(msg, onlineUser.getSession());
    }

    private static void sendOpenChatCommand(OnlineUser user, Chat chat) {
        try {
            JSONObject primeData = new JSONObject().put("chatID", chat.getID()).put("name", chat.getName());
            if (chat.getID().equals(LobbyID))
                primeData.put("type", "global");
            JSONObject json = buildMessage("tabControl", primeData, "open");
            sendMessageToUser(json, user);
        } catch (JSONException e) {
            LOGGER.debug(e);
        }
    }

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
        JSONObject json = buildTextMessage(msg);

        Chat chat = chats.getChatByID(chatID);
        if (chat instanceof Group)
            ((Group) chat).getHistory().add(msg);
        sendMessageToChat(json, chatID);
    }

    /**
     * Sendet die Nachricht an eine bestimmte Session, also einem Benutzer.
     * <p>
     *
     * @param session ist die WebSocketSession an welche das gesendet wird.
     * @param msg     ist die zu sendene Nachricht.
     */
    public static void sendMessageToSession(JSONObject msg, Session session) {
        if (session != null)
            try {
                session.getRemote().sendString(msg.toString());
            } catch (IOException e) {
                LOGGER.debug(e);
            }
    }
    //endregion

    //region Chats

    /**
     * Erstellt einen neuen Chat
     * <p>
     *
     * @param name  ist der Name den der neue Chat haben soll
     * @param users sind die User, die zu dem neuen Chat hinzugefügt werden sollen
     *              <p>
     * @return die ChatID
     */
    public static String createGroup(String name, Collection<User> users) {
        return chats.createGroup(name, users);
    }

    public static void inviteUserToChat(String chatID) {
        Chat chat = chats.getChatByID(chatID);
        chats.getChatByID(chatID).forEach(user -> {
            sendOpenChatCommand(onlineUsers.get(user), chat);
            if (chat instanceof Group)
                user.getGroups().add(chatID);
        });
    }
    //endregion

    //region Other

    /**
     * Gibt die Liste der User, welche online sind zurück.
     * <p>
     *
     * @return die {@link User} in einem {@link UserRepository}
     */
    public static UserRepository<OnlineUser> getOnlineUsers() {
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

    //endregion
}
