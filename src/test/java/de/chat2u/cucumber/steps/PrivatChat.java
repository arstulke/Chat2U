package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebenseien;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.Permissions;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.model.Chat;
import de.chat2u.model.Message;
import de.chat2u.utils.MessageBuilder;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;

import static org.mockito.Mockito.*;

/**
 * Created de.chat2u.cucumber.steps.PrivatChat in PACKAGE_NAME
 * by ARSTULKE on 22.11.2016.
 */
public class PrivatChat {

    private AuthenticationUser user1 = new AuthenticationUser("user1", "user1_pass", Permissions.USER);
    private AuthenticationUser user2 = new AuthenticationUser("user2", "user2_pass", Permissions.USER);
    private AuthenticationUser user3 = new AuthenticationUser("user3", "user3_pass", Permissions.USER);
    private Message msg;

    @Gegebenseien("^drei angemeldete Benutzer$")
    public void dreiAngemeldeteBenutzer() throws Throwable {
        UserRepository<AuthenticationUser> repo = new UserRepository<>();
        repo.addUser(user1);
        repo.addUser(user2);
        repo.addUser(user3);
        ChatServer.initialize(new AuthenticationService(repo));

        Session session1 = mock(Session.class);
        Session session2 = mock(Session.class);
        Session session3 = mock(Session.class);
        RemoteEndpoint remote1 = mock(RemoteEndpoint.class);
        RemoteEndpoint remote2 = mock(RemoteEndpoint.class);
        RemoteEndpoint remote3 = mock(RemoteEndpoint.class);
        when(session1.getRemote()).thenReturn(remote1);
        when(session2.getRemote()).thenReturn(remote2);
        when(session3.getRemote()).thenReturn(remote3);

        ChatServer.login(user1.getUsername(), user1.getPassword(), session1);
        ChatServer.login(user2.getUsername(), user2.getPassword(), session2);
        ChatServer.login(user3.getUsername(), user3.getPassword(), session3);
    }

    @Wenn("^der Erste dem Zweiten eine Nachricht im privat Chat sendet$")
    public void derErsteDemZweitenEineNachrichtImPrivatChatSendet() throws Throwable {
        String msg = "Eine Nachricht";

        //chat erstellen
        Chat chat = new Chat(false, user1, user2);
        String chatID = ChatServer.createChat(chat.getUsers());

        //nachricht senden
        this.msg = new Message(user1.getUsername(), msg, chatID);
        ChatServer.sendMessageToChat(user1.getUsername(), msg, chatID, "msg");
    }

    @Dann("^wird diese nur im Chat des Ersten und des Zweiten sichtbar$")
    public void wirdDieseNurImChatDesErstenUndDesZweitenSichtbar() throws Throwable {
        RemoteEndpoint remote1 = ChatServer.getOnlineUsers().getByUsername(user1.getUsername()).getSession().getRemote();
        RemoteEndpoint remote2 = ChatServer.getOnlineUsers().getByUsername(user2.getUsername()).getSession().getRemote();
        verify(remote1).sendString(MessageBuilder.buildMessage(msg, "msg").toString());
        verify(remote2).sendString(MessageBuilder.buildMessage(msg, "msg").toString());
    }

    @Und("^nicht im Chat des Dritten$")
    public void nichtImChatDesDritten() throws Throwable {
        RemoteEndpoint remote3 = ChatServer.getOnlineUsers().getByUsername(user3.getUsername()).getSession().getRemote();
        verify(remote3, never()).sendString(MessageBuilder.buildMessage(msg, "msg").toString());
    }
}
